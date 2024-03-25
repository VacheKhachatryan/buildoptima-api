package com.vecondev.buildoptima.parameters.user;

import static com.vecondev.buildoptima.exception.Error.USER_NOT_FOUND;
import static com.vecondev.buildoptima.filter.model.SearchOperation.EQ;
import static com.vecondev.buildoptima.filter.model.SearchOperation.GT;
import static com.vecondev.buildoptima.filter.model.SearchOperation.LIKE;
import static com.vecondev.buildoptima.model.user.Role.ADMIN;
import static com.vecondev.buildoptima.model.user.Role.CLIENT;

import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.dto.user.request.AuthRequestDto;
import com.vecondev.buildoptima.dto.user.request.ChangePasswordRequestDto;
import com.vecondev.buildoptima.dto.user.request.RefreshTokenRequestDto;
import com.vecondev.buildoptima.dto.user.request.UserRegistrationRequestDto;
import com.vecondev.buildoptima.exception.UserNotFoundException;
import com.vecondev.buildoptima.filter.model.Criteria;
import com.vecondev.buildoptima.filter.model.SortDto;
import com.vecondev.buildoptima.model.user.ConfirmationToken;
import com.vecondev.buildoptima.model.user.RefreshToken;
import com.vecondev.buildoptima.model.user.Role;
import com.vecondev.buildoptima.model.user.User;
import com.vecondev.buildoptima.repository.user.ConfirmationTokenRepository;
import com.vecondev.buildoptima.repository.user.RefreshTokenRepository;
import com.vecondev.buildoptima.repository.user.UserRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import javax.validation.UnexpectedTypeException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.mock.web.MockMultipartFile;

@AllArgsConstructor
@NoArgsConstructor
public class UserControllerTestParameters extends UserTestParameters {

  private static final String TEST_IMAGES_PATH = "src/test/resources/images/";
  private UserRepository userRepository;
  private ConfirmationTokenRepository confirmationTokenRepository;
  private RefreshTokenRepository refreshTokenRepository;

  public List<User> users() {
    return List.of(
        new User("John", "Smith", "+712345678", "john@mail.ru", "John1234.", CLIENT, true, 0),
        new User("John", "Stone", "+612345678", "john@gmail.com", "John1234/", ADMIN, true, 0),
        new User(
            "Melissa", "Jones", "+812345678", "melissa@gmail.com", "Mellisa1234/", CLIENT, true, 0),
        new User(
            "Olivia", "Murphy", "+512345678", "olivia@gmail.com", "Olivia1234/", CLIENT, true, 0),
        new User("Jack", "Williams", "+212345678", "jack@gmail.com", "Jack1234/", CLIENT, true, 0),
        new User("Emily", "Brown", "+312345678", "emily@gmail.com", "Emily1234/", CLIENT, true, 0),
        new User("Harry", "Taylor", "+112345678", "harry@gmail.com", "Harry1234/", CLIENT, true, 0),
        new User("Lily", "Wilson", "+412345678", "lily@gmail.com", "Lily1234/", CLIENT, true, 0),
        new User(
            "Thomas", "Williams", "+912345678", "thom@gmail.com", "Thom1234/", CLIENT, false, 0),
        new User(
            "Olivia", "Taylor", "+1012345678", "olivia@mail.ru", "Olivia1234.", CLIENT, false, 0));
  }

  public List<ConfirmationToken> confirmationTokens() {
    List<ConfirmationToken> confirmationTokens = new ArrayList<>();

    userRepository.findAll().stream()
        .filter(User::isEnabled)
        .forEach(
            user ->
                confirmationTokens.add(
                    new ConfirmationToken(
                        UUID.randomUUID().toString(),
                        LocalDateTime.ofInstant(user.getCreatedAt(), ZoneId.systemDefault())
                            .plusMonths(6),
                        user)));

    return confirmationTokens;
  }

  public List<RefreshToken> refreshTokens() {
    List<RefreshToken> refreshTokens = new ArrayList<>();

    userRepository.findAll().stream()
        .filter(User::isEnabled)
        .forEach(
            user ->
                refreshTokens.add(
                    new RefreshToken(
                        UUID.randomUUID().toString(),
                        user.getId(),
                        LocalDateTime.ofInstant(user.getCreatedAt(), ZoneId.systemDefault())
                            .plusMonths(6))));

    refreshTokens.stream()
        .findAny()
        .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND))
        .setExpiresAt(LocalDateTime.now().minusDays(2));

    return refreshTokens;
  }

  public UserRegistrationRequestDto getUserToSave() {
    return new UserRegistrationRequestDto(
        "John", "Smith", "+3741234567", "john12@gmail.com", "John1234.");
  }

  public UserRegistrationRequestDto getUserToSaveWithInvalidFields() {
    return new UserRegistrationRequestDto(
        "John8", "Smith8", "+3741234567a", "john12@gmail", "John1234");
  }

  public UserRegistrationRequestDto getUserToSaveWithDuplicatedEmail() {
    User savedUser =
        users().stream().findAny().orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    return new UserRegistrationRequestDto(
        "John", "Smith", "+3741234567", savedUser.getEmail(), "John1234.");
  }

  public ConfirmationToken getConfirmationTokenToConfirmAccount() {
    return confirmationTokenRepository.findAll().stream()
        .filter(token -> token.getExpiresAt().isAfter(LocalDateTime.now()))
        .findAny()
        .orElseThrow(NoSuchElementException::new);
  }

  public AuthRequestDto getUserCredentialsToLogin() {
    User savedUser =
        users().stream()
            .filter(User::isEnabled)
            .findAny()
            .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    return new AuthRequestDto(savedUser.getEmail(), savedUser.getPassword());
  }

  public AuthRequestDto getUserInvalidCredentialsToLogin() {
    return new AuthRequestDto("Peter@mail.ru", "Peter1234/");
  }

  public RefreshTokenRequestDto getRefreshToken() {
    RefreshToken refreshToken =
        refreshTokenRepository.findAll().stream()
            .filter(token -> token.getExpiresAt().isAfter(LocalDateTime.now()))
            .findAny()
            .orElseThrow(NoSuchElementException::new);

    return new RefreshTokenRequestDto(refreshToken.getPlainRefreshToken());
  }

  public RefreshTokenRequestDto getExpiredRefreshToken() {
    RefreshToken refreshToken =
        refreshTokenRepository.findAll().stream()
            .filter(token -> token.getExpiresAt().isBefore(LocalDateTime.now()))
            .findAny()
            .orElseThrow(NoSuchElementException::new);

    return new RefreshTokenRequestDto(refreshToken.getPlainRefreshToken());
  }

  public FetchRequestDto getInvalidFetchRequest() {
    return new FetchRequestDto(
        Map.of(
            "and",
            List.of(
                new Criteria(EQ, "firstNam", "John"),
                Map.of(
                    "or",
                    List.of(
                        new Criteria(LIKE, "lastNam", "Smith"),
                        new Criteria(GT, "creationDat", "2018-11-30T18:35:24.00Z"))))),
        0,
        10,
        List.of(new SortDto("firstName", SortDto.Direction.ASC)));
  }

  public ChangePasswordRequestDto getChangePasswordRequestDto(User user) {
    return new ChangePasswordRequestDto(user.getPassword(), user.getPassword() + "/a");
  }

  public User getUser() {
    return users().stream().findAny().orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
  }

  public User getUser(Role role) {
    return users().stream()
        .filter(user -> user.getRole() == role)
        .findAny()
        .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
  }

  public User getUserByEmail(String email) {
    return users().stream()
        .filter(user -> user.getEmail().equals(email))
        .findFirst()
        .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
  }

  public User getSavedUser() {
    return userRepository.findAll().stream()
        .findAny()
        .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
  }

  public User getSavedUser(Role role) {
    return userRepository.findAll().stream()
        .filter(user -> user.getRole() == role)
        .findAny()
        .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
  }

  public User getSavedUserWithId(UUID userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
  }

  public MockMultipartFile getMultiPartFile(String name, String contentType) {
    Path path = Paths.get(TEST_IMAGES_PATH + name);
    byte[] content;

    try {
      content = Files.readAllBytes(path);
    } catch (IOException e) {
      throw new UnexpectedTypeException();
    }

    return new MockMultipartFile("file", name, contentType, content);
  }
}
