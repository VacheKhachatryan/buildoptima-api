package com.vecondev.buildoptima.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.dto.filter.FetchResponseDto;
import com.vecondev.buildoptima.dto.user.request.ChangePasswordRequestDto;
import com.vecondev.buildoptima.dto.user.request.EditUserDto;
import com.vecondev.buildoptima.dto.user.response.UserResponseDto;
import com.vecondev.buildoptima.exception.AuthenticationException;
import com.vecondev.buildoptima.exception.UserNotFoundException;
import com.vecondev.buildoptima.filter.converter.PageableConverter;
import com.vecondev.buildoptima.mapper.user.UserMapper;
import com.vecondev.buildoptima.model.user.ConfirmationToken;
import com.vecondev.buildoptima.model.user.User;
import com.vecondev.buildoptima.parameters.user.UserServiceTestParameters;
import com.vecondev.buildoptima.repository.user.UserRepository;
import com.vecondev.buildoptima.service.auth.AuthService;
import com.vecondev.buildoptima.service.auth.ConfirmationTokenService;
import com.vecondev.buildoptima.service.auth.SecurityContextService;
import com.vecondev.buildoptima.service.s3.AmazonS3Service;
import com.vecondev.buildoptima.service.sqs.SqsService;
import com.vecondev.buildoptima.service.user.UserServiceImpl;
import com.vecondev.buildoptima.util.RestPreconditions;
import com.vecondev.buildoptima.validation.validator.FieldNameValidator;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  private final UserServiceTestParameters testParameters = new UserServiceTestParameters();

  @InjectMocks private UserServiceImpl userService;
  @Mock private AmazonS3Service imageService;
  @Mock private UserMapper userMapper;
  @Mock private AuthService authService;
  @Mock private SqsService sqsService;
  @Mock private ConfirmationTokenService confirmationTokenService;
  @Mock private UserRepository userRepository;
  @Mock private PasswordEncoder encoder;
  @Mock private PageableConverter pageableConverter;
  @Mock private SecurityContextService securityContextService;

  @Test
  void successfulFetchingOfUsers() {
    FetchRequestDto requestDto = testParameters.getFetchRequest();
    Pageable pageable = testParameters.getPageable(requestDto);
    final Page<User> result = new PageImpl<>(testParameters.getUserList());

    when(securityContextService.getUserDetails()).thenReturn(testParameters.userDetails());
    try (MockedStatic<FieldNameValidator> validator =
        Mockito.mockStatic(FieldNameValidator.class)) {
      validator
          .when(() -> FieldNameValidator.validateFieldNames(any(), any()))
          .thenAnswer((Answer<Void>) invocation -> null);
    }
    when(pageableConverter.convert(requestDto)).thenReturn(pageable);
    when(userRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(result);
    when(userMapper.mapToResponseList(result))
        .thenReturn(testParameters.getUserResponseDtoList(result.stream().toList()));

    FetchResponseDto responseDto = userService.fetch(requestDto);
    assertEquals(2, responseDto.getTotalElements());
  }

  @Test
  void successfulFetchingWithDefaultSortDirectory() {
    FetchRequestDto requestDto = testParameters.getFetchRequest();
    requestDto.setSort(null);
    Pageable pageable = testParameters.getPageable(requestDto);
    final Page<User> result = new PageImpl<>(testParameters.getUserList());

    when(securityContextService.getUserDetails()).thenReturn(testParameters.userDetails());
    try (MockedStatic<FieldNameValidator> validator =
        Mockito.mockStatic(FieldNameValidator.class)) {
      validator
          .when(() -> FieldNameValidator.validateFieldNames(any(), any()))
          .thenAnswer((Answer<Void>) invocation -> null);
    }
    when(pageableConverter.convert(requestDto)).thenReturn(pageable);
    when(userRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(result);
    when(userMapper.mapToResponseList(result))
        .thenReturn(testParameters.getUserResponseDtoList(result.stream().toList()));

    FetchResponseDto responseDto = userService.fetch(requestDto);
    assertEquals(2, responseDto.getTotalElements());
  }

  @Test
  void successfulChangingOfPassword() {
    String oldPassword = "oldPassword";
    final String newPassword = "newPassword";
    User user = testParameters.getSavedUser();
    user.setPassword(testParameters.getPasswordEncoded(oldPassword));

    when(securityContextService.getUserDetails()).thenReturn(testParameters.userDetails());
    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    when(encoder.matches(any(), any())).thenReturn(true);
    when(encoder.encode(any())).thenReturn(testParameters.getPasswordEncoded(newPassword));

    assertDoesNotThrow(
        () ->
            userService.changePassword(
                testParameters.getChangePasswordRequestDto(oldPassword, newPassword)));
    verify(encoder).encode(any());
  }

  @Test
  void failedChangingOfPasswordAsPasswordIsInvalid() {
    String oldPassword = "oldPassword";
    User user = testParameters.getSavedUser();
    user.setPassword(testParameters.getPasswordEncoded(oldPassword + 1));
    final ChangePasswordRequestDto requestDto =
        testParameters.getChangePasswordRequestDto(oldPassword, "newPassword");

    when(securityContextService.getUserDetails()).thenReturn(testParameters.userDetails());
    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    when(encoder.matches(any(), any())).thenReturn(false);

    assertThrows(AuthenticationException.class, () -> userService.changePassword(requestDto));
  }

  @Test
  void failedChangingOfPasswordAsPasswordsAreTheSame() {
    String password = "oldPassword";
    User user = testParameters.getSavedUser();
    user.setPassword(testParameters.getPasswordEncoded(password));
    final ChangePasswordRequestDto requestDto =
        testParameters.getChangePasswordRequestDto(password, password);

    when(securityContextService.getUserDetails()).thenReturn(testParameters.userDetails());
    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    when(encoder.matches(any(), any())).thenReturn(true);

    assertThrows(AuthenticationException.class, () -> userService.changePassword(requestDto));
  }

  @Test
  void successfulUserFetching() {
    User user = testParameters.getSavedUser();

    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(userMapper.mapToResponseDto(user)).thenReturn(testParameters.getUserResponseDto(user));

    UserResponseDto responseDto = userService.getById(user.getId());
    assertEquals(user.getId(), responseDto.getId());
    assertEquals(user.getUpdatedAt(), responseDto.getUpdatedAt());
  }

  @Test
  void failedUserFetching() {
    UUID userId = UUID.randomUUID();

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.getById(userId));
  }

  @Test
  void successfulImageUploading() {
    UUID userId = UUID.randomUUID();
    User user = testParameters.getSavedUser();
    user.setId(userId);

    try (MockedStatic<RestPreconditions> restPreconditions =
        Mockito.mockStatic(RestPreconditions.class)) {
      restPreconditions
          .when(() -> RestPreconditions.checkNotNull(any(), any()))
          .thenAnswer((Answer<Void>) invocation -> null);
      when(userRepository.findById(userId)).thenReturn(Optional.of(user));
      userService.uploadImage(userId, null);
    }

    user.setImageVersion(user.getImageVersion() + 1);
    verify(userRepository).saveAndFlush(user);
  }

  @Test
  void successfulImageDownloading() {
    UUID ownerId = UUID.randomUUID();
    String contentType = IMAGE_JPEG_VALUE;
    boolean isOriginal = true;
    String className = User.class.getSimpleName().toLowerCase();

    when(securityContextService.getUserDetails()).thenReturn(testParameters.userDetails());
    when(userRepository.findById(any())).thenReturn(Optional.of(testParameters.getSavedUser()));
    when(imageService.downloadImage(className, ownerId, 1, isOriginal)).thenReturn(new byte[] {});
    when(imageService.getContentTypeOfObject(className, ownerId, 1, isOriginal))
        .thenReturn(contentType);
    ResponseEntity<byte[]> response = userService.downloadImage(ownerId, isOriginal);

    assertEquals(
        contentType, Objects.requireNonNull(response.getHeaders().get("Content-type")).get(0));
    verify(imageService).downloadImage(className, ownerId, 1, isOriginal);
  }

  @Test
  void successfulImageDeleting() {
    when(userRepository.findById(any())).thenReturn(Optional.of(testParameters.getSavedUser()));
    userService.deleteImage(UUID.randomUUID());

    verify(imageService).deleteImagesFromS3(any(), any(), any());
  }

  @Test
  void failedImageDeletingAsObjectDoesntExist() {
    UUID userId = UUID.randomUUID();
    when(userRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.deleteImage(userId));
  }

  @Test
  void editUserSuccess() {
    User userToEdit = testParameters.getSavedUser();
    UUID userId = userToEdit.getId();
    EditUserDto editUserDto = testParameters.editUserDto();
    UserResponseDto responseDto =
        testParameters.getUserResponseDto(testParameters.editedUser(editUserDto));
    ConfirmationToken confirmationToken = testParameters.confirmationToken(userToEdit);

    when(userRepository.findById(userId)).thenReturn(Optional.of(userToEdit));
    when(userMapper.mapToResponseDto(any(User.class))).thenReturn(responseDto);
    when(confirmationTokenService.create(any(User.class))).thenReturn(confirmationToken);

    UserResponseDto editedUserDto = userService.edit(userId, editUserDto);
    assertNotNull(editedUserDto);
    assertEquals(editedUserDto.getFirstName(), editUserDto.getFirstName());
  }

  @Test
  void editUserSuccessSameEmail() {
    User userToEdit = testParameters.getSavedUser();
    UUID userId = userToEdit.getId();
    EditUserDto editUserDto = testParameters.editUserDto();
    editUserDto.setEmail(userToEdit.getEmail());
    UserResponseDto responseDto =
        testParameters.getUserResponseDto(testParameters.editedUser(editUserDto));

    when(userRepository.findById(userId)).thenReturn(Optional.of(userToEdit));
    when(userMapper.mapToResponseDto(any(User.class))).thenReturn(responseDto);

    UserResponseDto editedUserDto = userService.edit(userId, editUserDto);
    assertNotNull(editedUserDto);
    assertEquals(editedUserDto.getFirstName(), editUserDto.getFirstName());
  }
}
