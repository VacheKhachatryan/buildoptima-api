package com.vecondev.buildoptima.parameters.user;

import static com.vecondev.buildoptima.model.user.Role.CLIENT;

import com.vecondev.buildoptima.dto.EntityOverview;
import com.vecondev.buildoptima.dto.user.request.ChangePasswordRequestDto;
import com.vecondev.buildoptima.dto.user.request.EditUserDto;
import com.vecondev.buildoptima.dto.user.request.UserRegistrationRequestDto;
import com.vecondev.buildoptima.dto.user.response.UserResponseDto;
import com.vecondev.buildoptima.model.user.ConfirmationToken;
import com.vecondev.buildoptima.model.user.RefreshToken;
import com.vecondev.buildoptima.model.user.User;
import com.vecondev.buildoptima.parameters.PageableTest;
import com.vecondev.buildoptima.security.user.AppUserDetails;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceTestParameters extends UserTestParameters implements PageableTest {
  private final PasswordEncoder encoder = new BCryptPasswordEncoder(12);

  public List<User> getUserList() {
    return List.of(
        User.builder().id(UUID.randomUUID()).firstName("John").lastName("Anderson").build(),
        User.builder().id(UUID.randomUUID()).firstName("John").lastName("Peterson").build());
  }

  public UserRegistrationRequestDto getUserRegistrationRequestDto() {
    return new UserRegistrationRequestDto(
        "Example", "Example", "+1234567890", "example@gmail.com", "Example1234.");
  }

  public User getUserFromRegistrationDto(UserRegistrationRequestDto requestDto) {
    return new User(
        requestDto.getFirstName(),
        requestDto.getLastName(),
        requestDto.getPhone(),
        requestDto.getEmail(),
        requestDto.getPassword(),
        CLIENT,
        false,
        0);
  }

  public User getSavedUser(User user) {
    User savedUser =
        user.toBuilder().password(encoder.encode(user.getPassword())).enabled(true).build();
    savedUser.setId(UUID.randomUUID());

    return savedUser;
  }

  public User getSavedUser() {
    User user =
        new User(
            "Example",
            "Example",
            "+1234567890",
            "Example@gmail.com",
            encoder.encode("Example1234."),
            CLIENT,
            true,
            1);
    user.setId(UUID.randomUUID());
    return user;
  }

  public UserResponseDto getUserResponseDto(User user) {
    return new UserResponseDto(
        user.getId(),
        user.getImageVersion(),
        user.getFirstName(),
        user.getLastName(),
        user.getPhone(),
        user.getEmail(),
        user.getRole(),
        user.getCreatedAt(),
        user.getUpdatedAt());
  }

  public EntityOverview getUserOverView(User user) {
    return new EntityOverview(
        user.getId(), String.format("%s %s", user.getFirstName(), user.getLastName()));
  }

  public List<UserResponseDto> getUserResponseDtoList(List<User> users) {
    return users.stream().map(this::getUserResponseDto).collect(Collectors.toList());
  }

  public ConfirmationToken getConfirmationToken(String token, User user) {
    return new ConfirmationToken(token, LocalDateTime.now().plusDays(1), user);
  }

  public RefreshToken getRefreshTokenWithRefreshTokenId(UUID userId, Boolean isDecoded) {
    RefreshToken refreshToken =
        new RefreshToken(UUID.randomUUID().toString(), userId, LocalDateTime.now().plusMonths(6));
    if (isDecoded) {
      refreshToken.setPlainRefreshToken(encoder.encode(refreshToken.getPlainRefreshToken()));
    }

    return refreshToken;
  }

  public ChangePasswordRequestDto getChangePasswordRequestDto(
      String oldPassword, String newPassword) {
    return new ChangePasswordRequestDto(oldPassword, newPassword);
  }

  public String getPasswordEncoded(String password) {
    return encoder.encode(password);
  }

  public ConfirmationToken getSavedConfirmationToken(User savedUser, UUID uuid) {
    return new ConfirmationToken(uuid.toString(), LocalDateTime.now().plusDays(1), savedUser);
  }

  public AppUserDetails userDetails() {
    User user = getSavedUser();
    return AppUserDetails.builder()
        .id(user.getId())
        .username(user.getEmail())
        .authorities(user.getRole().getAuthorities())
        .enabled(true)
        .build();
  }

  public EditUserDto editUserDto() {
    return EditUserDto.builder()
        .firstName("Jamila")
        .lastName("Ahmed")
        .email("jamilahmed@gmail.com")
        .phone("+37493776694")
        .build();
  }

  public EditUserDto editUserDtoInvalid() {
    return EditUserDto.builder().firstName("J").lastName("A").email("jam").phone("+374").build();
  }

  public User editedUser(EditUserDto editUserDto) {
    User savedUser = getSavedUser();
    savedUser.setFirstName(editUserDto.getFirstName());
    savedUser.setLastName(editUserDto.getLastName());
    savedUser.setEmail(editUserDto.getEmail());
    savedUser.setPhone(editUserDto.getPhone());
    return savedUser;
  }

  public ConfirmationToken confirmationToken(User user) {
    return ConfirmationToken.builder()
        .id(UUID.randomUUID())
        .user(user)
        .token(UUID.randomUUID().toString())
        .expiresAt(LocalDateTime.now().plusHours(5))
        .build();
  }
}
