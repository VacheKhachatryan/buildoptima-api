package com.vecondev.buildoptima.service.user;

import static com.vecondev.buildoptima.exception.Error.IMAGE_IS_REQUIRED;
import static com.vecondev.buildoptima.exception.Error.PROVIDED_SAME_PASSWORD;
import static com.vecondev.buildoptima.exception.Error.PROVIDED_WRONG_PASSWORD;
import static com.vecondev.buildoptima.exception.Error.USER_NOT_FOUND;
import static com.vecondev.buildoptima.filter.model.UserFields.userPageSortingFieldsMap;
import static com.vecondev.buildoptima.util.RestPreconditions.*;
import static com.vecondev.buildoptima.validation.validator.FieldNameValidator.validateFieldNames;

import com.vecondev.buildoptima.dto.ImageOverview;
import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.dto.filter.FetchResponseDto;
import com.vecondev.buildoptima.dto.user.ConfirmationMessage;
import com.vecondev.buildoptima.dto.user.request.ChangePasswordRequestDto;
import com.vecondev.buildoptima.dto.user.request.EditUserDto;
import com.vecondev.buildoptima.dto.user.response.UserResponseDto;
import com.vecondev.buildoptima.exception.AuthenticationException;
import com.vecondev.buildoptima.exception.Error;
import com.vecondev.buildoptima.exception.UserAlreadyExistException;
import com.vecondev.buildoptima.exception.UserNotFoundException;
import com.vecondev.buildoptima.filter.converter.PageableConverter;
import com.vecondev.buildoptima.filter.model.SortDto;
import com.vecondev.buildoptima.filter.specification.GenericSpecification;
import com.vecondev.buildoptima.mapper.user.UserMapper;
import com.vecondev.buildoptima.model.user.ConfirmationToken;
import com.vecondev.buildoptima.model.user.User;
import com.vecondev.buildoptima.repository.user.UserRepository;
import com.vecondev.buildoptima.security.user.AppUserDetails;
import com.vecondev.buildoptima.service.auth.ConfirmationTokenService;
import com.vecondev.buildoptima.service.auth.SecurityContextService;
import com.vecondev.buildoptima.service.s3.AmazonS3Service;
import com.vecondev.buildoptima.service.sqs.SqsService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final AmazonS3Service imageService;
  private final PageableConverter pageableConverter;
  private final SecurityContextService securityContextService;
  private final SqsService sqsService;
  private final ConfirmationTokenService confirmationTokenService;
  private static final String CONFIRM_TEMPLATE = "confirm.html";

  @Override
  public FetchResponseDto fetch(FetchRequestDto fetchRequest) {
    String username = securityContextService.getUserDetails().getUsername();
    log.info("User {} is trying to fetch users", username);
    validateFieldNames(userPageSortingFieldsMap, fetchRequest.getSort());
    if (fetchRequest.getSort() == null || fetchRequest.getSort().isEmpty()) {
      SortDto sortDto = new SortDto("firstName", SortDto.Direction.ASC);
      fetchRequest.setSort(List.of(sortDto));
    }
    Pageable pageable = pageableConverter.convert(fetchRequest);
    Specification<User> specification =
        new GenericSpecification<>(userPageSortingFieldsMap, fetchRequest.getFilter());

    assert pageable != null;
    Page<User> result = userRepository.findAll(specification, pageable);

    List<UserResponseDto> content = userMapper.mapToResponseList(result);
    log.info("Response was sent. {} results where found", content.size());
    return FetchResponseDto.builder()
        .content(content)
        .page(result.getNumber())
        .size(result.getSize())
        .totalElements(result.getTotalElements())
        .last(result.isLast())
        .build();
  }

  @Override
  public void changePassword(ChangePasswordRequestDto request) {
    String username = securityContextService.getUserDetails().getUsername();
    log.info("Request from user {} to change the password", username);
    User user =
        userRepository
            .findByEmail(username)
            .orElseThrow(() -> new AuthenticationException(USER_NOT_FOUND));
    if (!isValidPassword(request, user)) {
      log.warn("User {} had provided wrong credentials to change the password", user.getEmail());
      throw new AuthenticationException(PROVIDED_WRONG_PASSWORD);
    }
    if (request.getOldPassword().equals(request.getNewPassword())) {
      log.warn("In change password request user {} provided the same password", user.getEmail());
      throw new AuthenticationException(PROVIDED_SAME_PASSWORD);
    }
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    log.info("User {} password was successfully changed", user.getEmail());
  }

  @Override
  public UserResponseDto edit(UUID id, EditUserDto editUserDto) {
    User user =
        userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

    validate(user, editUserDto);

    user.setFirstName(editUserDto.getFirstName());
    user.setLastName(editUserDto.getLastName());
    user.setPhone(editUserDto.getPhone());

    String oldEmail = user.getEmail();
    String newEmail = editUserDto.getEmail();

    if (!oldEmail.equalsIgnoreCase(newEmail)) {
      user.setEmail(newEmail);
      user.setEnabled(false);
      sendEmail(user, confirmationTokenService.create(user));
    }
    return userMapper.mapToResponseDto(user);
  }

  @Override
  public UserResponseDto getById(UUID userId) {
    log.info("Request to get user profile by id");
    User user = findUserById(userId);
    log.info("Fetched user {} profile", user.getEmail());

    return userMapper.mapToResponseDto(user);
  }

  @Override
  public UserResponseDto getCurrentUser() {
    AppUserDetails userDetails = securityContextService.getUserDetails();

    return userMapper.mapToResponseDto(
        userRepository
            .findById(userDetails.getId())
            .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND)));
  }

  /**
   * Uploads new image or updates existing one, saves the original one 'and' it's thumbnail version
   * as well.
   *
   * @param multipartFile file representing the image
   */
  @Override
  public ImageOverview uploadImage(UUID userId, MultipartFile multipartFile) {
    checkNotNull(multipartFile, IMAGE_IS_REQUIRED);
    User user = findUserById(userId);
    imageService.uploadImagesToS3("user", userId, user.getImageVersion(), multipartFile, userId);

    user.setImageVersion(user.getImageVersion() + 1);
    user = userRepository.saveAndFlush(user);
    return userMapper.mapToUserImageOverview(user);
  }

  /**
   * Downloads image.
   *
   * @param ownerId the image owner
   * @param isOriginal flag that shows if image is original or not (thumbnail)
   */
  @Override
  public ResponseEntity<byte[]> downloadImage(UUID ownerId, boolean isOriginal) {
    log.info(
        "User {} trying to download original image of user with id: {}.",
        securityContextService.getUserDetails().getUsername(),
        ownerId);
    Integer userImageVersion = findUserById(ownerId).getImageVersion();
    byte[] imageAsByteArray =
        imageService.downloadImage(
            User.class.getSimpleName().toLowerCase(), ownerId, userImageVersion, isOriginal);
    String contentType =
        imageService.getContentTypeOfObject("user", ownerId, userImageVersion, isOriginal);

    return ResponseEntity.ok()
        .contentLength(imageAsByteArray.length)
        .header("Content-type", contentType)
        .header(
            "Content-disposition",
            String.format(
                "attachment; filename=image%s.%s",
                isOriginal ? "" : "-100X100", contentType.substring(contentType.indexOf("/") + 1)))
        .body(imageAsByteArray);
  }

  @Override
  public void deleteImage(UUID userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    String className = User.class.getSimpleName().toLowerCase();

    imageService.checkExistenceOfObject(
        imageService.getImagePath(className, userId, user.getImageVersion(), true), userId);
    imageService.checkExistenceOfObject(
        imageService.getImagePath(className, userId, user.getImageVersion(), false), userId);
    imageService.deleteImagesFromS3("user", userId, user.getImageVersion());
  }

  @Override
  public User findUserById(UUID userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
  }

  private boolean isValidPassword(ChangePasswordRequestDto request, User user) {
    return passwordEncoder.matches(request.getOldPassword(), user.getPassword());
  }

  private void sendEmail(User user, ConfirmationToken confirmationToken) {
    sqsService.sendMessage(
        ConfirmationMessage.builder()
            .template(CONFIRM_TEMPLATE)
            .token(confirmationToken.getToken())
            .userFirstName(user.getFirstName())
            .userEmail(user.getEmail())
            .build()
            .toString());
  }

  private void validate(User user, EditUserDto editUserDto) {
    String oldEmail = user.getEmail();
    String newEmail = editUserDto.getEmail();

    String oldPhone = user.getPhone();
    String newPhone = editUserDto.getPhone();

    if (!oldEmail.equalsIgnoreCase(newEmail)
        && userRepository.existsByEmailIgnoreCase(editUserDto.getEmail())) {
      throw new UserAlreadyExistException(Error.USER_ALREADY_EXIST_WITH_EMAIL);
    }
    if (!oldPhone.equals(newPhone) && userRepository.existsByPhone(editUserDto.getPhone())) {
      throw new UserAlreadyExistException(Error.USER_ALREADY_EXIST_WITH_PHONE);
    }
  }
}
