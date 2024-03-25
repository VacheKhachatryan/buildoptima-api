package com.vecondev.buildoptima.api;

import static com.vecondev.buildoptima.model.user.Role.ADMIN;
import static com.vecondev.buildoptima.model.user.Role.CLIENT;
import static com.vecondev.buildoptima.util.FileUtil.convertMultipartFileToFile;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.vecondev.buildoptima.actions.UserResultActions;
import com.vecondev.buildoptima.config.properties.S3ConfigProperties;
import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.dto.user.request.ChangePasswordRequestDto;
import com.vecondev.buildoptima.endpoints.UserEndpointUris;
import com.vecondev.buildoptima.model.user.User;
import com.vecondev.buildoptima.parameters.user.UserControllerTestParameters;
import com.vecondev.buildoptima.repository.user.ConfirmationTokenRepository;
import com.vecondev.buildoptima.repository.user.RefreshTokenRepository;
import com.vecondev.buildoptima.repository.user.UserRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith({SpringExtension.class})
@ActiveProfiles("test")
@Import({UserEndpointUris.class, UserResultActions.class})
class UserControllerTest {

  @Autowired private AmazonS3 amazonS3;
  @Autowired private UserRepository userRepository;
  @Autowired private ConfirmationTokenRepository confirmationTokenRepository;
  @Autowired private RefreshTokenRepository refreshTokenRepository;
  @Autowired private PasswordEncoder encoder;
  @Autowired private S3ConfigProperties s3ConfigProperties;
  @Autowired private UserResultActions resultActions;

  private UserControllerTestParameters userControllerTestParameters;

  @BeforeEach
  void setUp() {
    userControllerTestParameters =
        new UserControllerTestParameters(
            userRepository, confirmationTokenRepository, refreshTokenRepository);

    List<User> users = userControllerTestParameters.users();
    users.forEach(user -> user.setPassword(encoder.encode(user.getPassword())));
    userRepository.saveAll(users);

    confirmationTokenRepository.saveAll(userControllerTestParameters.confirmationTokens());
    refreshTokenRepository.saveAll(userControllerTestParameters.refreshTokens());
  }

  @AfterEach
  void tearDown() {
    refreshTokenRepository.deleteAll();
    confirmationTokenRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void successfulFetchingOfUsers() throws Exception {
    FetchRequestDto requestDto = userControllerTestParameters.getFetchRequest();
    User admin = userControllerTestParameters.getSavedUser(ADMIN);

    resultActions.fetch(requestDto, admin).andExpect(status().isOk());
  }

  @Test
  void failedFetchingOfUsersAsPermissionDenied() throws Exception {
    FetchRequestDto requestDto = userControllerTestParameters.getFetchRequest();
    User client = userControllerTestParameters.getSavedUser(CLIENT);

    resultActions.fetch(requestDto, client).andExpect(status().isForbidden());
  }

  @Test
  void failedFetchingOfUsersAsRequestDtoIsInvalid() throws Exception {
    FetchRequestDto requestDto = userControllerTestParameters.getInvalidFetchRequest();
    User admin = userControllerTestParameters.getSavedUser(ADMIN);

    resultActions.fetch(requestDto, admin).andExpect(status().isBadRequest());
  }

  @Test
  void successfulPasswordChanging() throws Exception {
    User savedUser = userControllerTestParameters.getSavedUser();
    savedUser.setPassword(
        userControllerTestParameters.getUserByEmail(savedUser.getEmail()).getPassword());
    ChangePasswordRequestDto requestDto =
        userControllerTestParameters.getChangePasswordRequestDto(savedUser);

    resultActions.changePassword(requestDto, savedUser).andExpect(status().isOk());
  }

  @Test
  void successfulGettingUser() throws Exception {
    User user = userControllerTestParameters.getSavedUser();
    User adminUser = userControllerTestParameters.getSavedUser(ADMIN);

    resultActions
        .getById(user.getId(), adminUser)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(user.getId().toString()));
  }

  @Test
  void failedGettingUserAsPermissionDenied() throws Exception {
    User user = userControllerTestParameters.getSavedUser();

    resultActions.getById(UUID.randomUUID(), user).andExpect(status().isForbidden());
  }

  @Nested
  class ImageTest {

    private static final String ORIGINAL_IMAGES_PATH = "user/%s/original/%s";
    private static final String THUMBNAIL_IMAGES_PATH = "user/%s/thumbnail/%s";
    private static final String ORIGINAL_IMAGE_TYPE_NAME = "image";
    private static final String THUMBNAIL_IMAGE_TYPE_NAME = "thumbnail-image";
    private static final String[] TEST_IMAGES = {"valid_image.jpg", "invalid_image_size.jpg"};

    @BeforeEach
    void setUp() {
      cleanS3Folder();
    }

    @AfterAll
    static void afterAll() throws IOException {
      for (String image : TEST_IMAGES) {
        Files.deleteIfExists(Paths.get(image));
      }
    }

    @Test
    void successfulImageUploading() throws Exception {
      User user = userControllerTestParameters.getSavedUser();
      UUID userId = user.getId();
      Integer imageVersion = user.getImageVersion();
      MockMultipartFile file =
          userControllerTestParameters.getMultiPartFile(TEST_IMAGES[0], IMAGE_JPEG_VALUE);

      resultActions.uploadImage(file, userId, user).andExpect(status().isOk());
      assertTrue(
          amazonS3.doesObjectExist(
              s3ConfigProperties.getImageBucketName(),
              String.format(ORIGINAL_IMAGES_PATH, userId, imageVersion + 1)));
      assertTrue(
          amazonS3.doesObjectExist(
              s3ConfigProperties.getImageBucketName(),
              String.format(THUMBNAIL_IMAGES_PATH, userId, imageVersion + 1)));
    }

    @Test
    void failedImageUploadingAsImageIsInvalid() throws Exception {
      User user = userControllerTestParameters.getSavedUser();
      UUID userId = user.getId();
      String filename = TEST_IMAGES[1];
      MockMultipartFile file =
          userControllerTestParameters.getMultiPartFile(filename, IMAGE_JPEG_VALUE);

      resultActions.uploadImage(file, userId, user).andExpect(status().isPreconditionFailed());
      assertFalse(
          amazonS3.doesObjectExist(
              s3ConfigProperties.getImageBucketName(),
              String.format(ORIGINAL_IMAGES_PATH, userId, user.getImageVersion() + 1)));
      assertFalse(
          amazonS3.doesObjectExist(
              s3ConfigProperties.getImageBucketName(),
              String.format(THUMBNAIL_IMAGES_PATH, userId, user.getImageVersion() + 1)));
      Files.delete(Paths.get(filename));
    }

    @Test
    void successfulOriginalImageDownloading() throws Exception {
      User savedUser = userControllerTestParameters.getSavedUser();
      UUID userId = savedUser.getId();
      Integer imageVersion = savedUser.getImageVersion() + 1;
      MultipartFile file =
          userControllerTestParameters.getMultiPartFile(TEST_IMAGES[0], IMAGE_JPEG_VALUE);
      amazonS3.putObject(
          s3ConfigProperties.getImageBucketName(),
          String.format(ORIGINAL_IMAGES_PATH, userId, imageVersion),
          convertMultipartFileToFile(file));
      savedUser.setImageVersion(imageVersion);
      userRepository.saveAndFlush(savedUser);

      resultActions
          .downloadImage(ORIGINAL_IMAGE_TYPE_NAME, userId, savedUser)
          .andExpect(status().isOk());
    }

    @Test
    void failedOriginalImageDownloadingAsPermissionDenied() throws Exception {
      resultActions
          .downloadImage(
              ORIGINAL_IMAGE_TYPE_NAME,
              UUID.randomUUID(),
              userControllerTestParameters.getSavedUser())
          .andExpect(status().isForbidden());
    }

    @Test
    void successfulThumbnailImageDownloading() throws Exception {
      User savedUser = userControllerTestParameters.getSavedUser();
      MultipartFile file =
          userControllerTestParameters.getMultiPartFile(TEST_IMAGES[0], IMAGE_JPEG_VALUE);
      amazonS3.putObject(
          s3ConfigProperties.getImageBucketName(),
          String.format(THUMBNAIL_IMAGES_PATH, savedUser.getId(), savedUser.getImageVersion() + 1),
          convertMultipartFileToFile(file));
      savedUser.setImageVersion(savedUser.getImageVersion() + 1);
      userRepository.saveAndFlush(savedUser);

      resultActions
          .downloadImage(THUMBNAIL_IMAGE_TYPE_NAME, savedUser.getId(), savedUser)
          .andExpect(status().isOk());
    }

    @Test
    void successfulImageDeletion() throws Exception {
      User savedUser = userControllerTestParameters.getSavedUser();
      UUID userId = savedUser.getId();
      Integer imageVersion = savedUser.getImageVersion() + 1;
      MultipartFile file =
          userControllerTestParameters.getMultiPartFile(TEST_IMAGES[0], IMAGE_JPEG_VALUE);

      amazonS3.putObject(
          s3ConfigProperties.getImageBucketName(),
          String.format(ORIGINAL_IMAGES_PATH, userId, imageVersion),
          convertMultipartFileToFile(file));
      amazonS3.putObject(
          s3ConfigProperties.getImageBucketName(),
          String.format(THUMBNAIL_IMAGES_PATH, userId, imageVersion),
          convertMultipartFileToFile(file));
      savedUser.setImageVersion(imageVersion);
      userRepository.saveAndFlush(savedUser);
      assumeTrue(
          amazonS3.doesObjectExist(
              s3ConfigProperties.getImageBucketName(),
              String.format(THUMBNAIL_IMAGES_PATH, userId, imageVersion)));

      resultActions.deleteImage(userId, savedUser).andExpect(status().isNoContent());
      assertFalse(
          amazonS3.doesObjectExist(
              s3ConfigProperties.getImageBucketName(),
              String.format(ORIGINAL_IMAGES_PATH, userId, imageVersion)));
      assertFalse(
          amazonS3.doesObjectExist(
              s3ConfigProperties.getImageBucketName(),
              String.format(THUMBNAIL_IMAGES_PATH, userId, imageVersion)));
    }

    @Test
    void failedImageDeletingAsImageDoesntExist() throws Exception {
      User savedUser = userControllerTestParameters.getSavedUser();
      UUID userId = savedUser.getId();

      assumeFalse(
          amazonS3.doesObjectExist(
              s3ConfigProperties.getImageBucketName(),
              String.format(ORIGINAL_IMAGES_PATH, userId, savedUser.getImageVersion())));
      assumeFalse(
          amazonS3.doesObjectExist(
              s3ConfigProperties.getImageBucketName(),
              String.format(THUMBNAIL_IMAGES_PATH, userId, savedUser.getImageVersion())));

      resultActions.deleteImage(userId, savedUser).andExpect(status().isNotFound());
    }

    private void cleanS3Folder() {
      String bucketName = s3ConfigProperties.getImageBucketName();
      for (S3ObjectSummary file :
          amazonS3
              .listObjects(bucketName, User.class.getSimpleName().toLowerCase())
              .getObjectSummaries()) {
        amazonS3.deleteObject(bucketName, file.getKey());
      }
    }
  }
}
