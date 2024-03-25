package com.vecondev.buildoptima.parameters.news;

import static com.vecondev.buildoptima.model.user.Role.ADMIN;
import static com.vecondev.buildoptima.model.user.Role.CLIENT;

import com.vecondev.buildoptima.dto.news.request.NewsCreateRequestDto;
import com.vecondev.buildoptima.dto.news.request.NewsUpdateRequestDto;
import com.vecondev.buildoptima.model.Status;
import com.vecondev.buildoptima.model.news.News;
import com.vecondev.buildoptima.model.news.NewsCategory;
import com.vecondev.buildoptima.model.user.User;
import java.time.Instant;
import lombok.NoArgsConstructor;
import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@NoArgsConstructor
public class NewsControllerTestParameters {

  private final PasswordEncoder encoder = new BCryptPasswordEncoder(12);

  public News getSavedNewsItem(NewsCreateRequestDto dto) {
    News news =
        News.builder()
            .title(dto.getTitle())
            .summary(dto.getSummary())
            .description(dto.getDescription())
            .status(Status.ACTIVE)
            .category(NewsCategory.valueOf(dto.getCategory()))
            .imageVersion(0)
            .build();
    news.setCreatedAt(Instant.now());
    news.setCreatedAt(Instant.now());
    return news;
  }

  public NewsCreateRequestDto getRequestToSave() {
    return NewsCreateRequestDto.builder()
        .title("Summer Sales")
        .summary("Steam Summer Sale 2022 continues — save big on top rated PC games")
        .description("stringstringstringstringstringstringstringstringst")
        .category("BREAKING_NEWS")
        .build();
  }

  public NewsCreateRequestDto getRequestToSaveWithInvalidFields() {
    return NewsCreateRequestDto.builder()
        .title("Sum")
        .summary("Steam Summer Sale 2022 continues — save big on top rated PC games")
        .description("stringstringstrt")
        .category("UNKNOWN_CATEGORY")
        .build();
  }

  public NewsUpdateRequestDto getRequestToUpdate() {
    return NewsUpdateRequestDto.builder()
        .title("Winter Sales")
        .summary("Steam Summer Sale 2022 continues — save big on top rated PC games")
        .description("stringstringssdfjeriugbeirgurgiurgu4g4iurg4irg49rhg0urgtrt")
        .category("OPINION")
        .build();
  }

  public NewsUpdateRequestDto getRequestToUpdateWithInvalidFields() {
    return NewsUpdateRequestDto.builder()
        .title("les")
        .summary("Steam Summer Sale 2022 continues — save big on top rated PC games")
        .description("stringstgtrt")
        .category("UNKNOWN_CATEGORY")
        .build();
  }

  public User getRightUser() {
    User user = new User();
    user.setFirstName("Example");
    user.setLastName("Example");
    user.setEmail("example@gmail.com");
    user.setPassword(encoder.encode("Example234."));
    user.setRole(ADMIN);
    user.setCreatedAt(Instant.now());
    user.setEnabled(true);
    user.setImageVersion(0);
    return user;
  }

  public User getWrongUser() {
    User user = new User();
    user.setFirstName("Example");
    user.setLastName("Example");
    user.setEmail("example@gmail.ru");
    user.setPassword(encoder.encode("Example234."));
    user.setRole(CLIENT);
    user.setCreatedAt(Instant.now());
    user.setEnabled(true);
    user.setImageVersion(0);
    return user;
  }

  public NewsCreateRequestDto createRequestDto() {
    NewsCreateRequestDto requestDto = new NewsCreateRequestDto();

    MockMultipartFile multipartFile =
        new MockMultipartFile(
            "image", "test.jpeg", ContentType.IMAGE_JPEG.toString(), "Spring Framework".getBytes());

    requestDto.setTitle("Summer Sales");
    requestDto.setSummary("Steam Summer Sale 2022 continues — save big on top rated PC games");
    requestDto.setDescription(
        "Steam Summer Sale 2022 continues — save big on top rated PC gamesSteam "
            + "Summer Sale 2022 continues — save big on top rated PC gamesSteam "
            + "Summer Sale 2022 continues — save big on top rated PC games");
    requestDto.setCategory("OPINION");
    requestDto.setImage(multipartFile);
    return requestDto;
  }

  public NewsCreateRequestDto createRequestDtoWithInvalidFields() {
    NewsCreateRequestDto requestDto = new NewsCreateRequestDto();

    MockMultipartFile multipartFile =
        new MockMultipartFile(
            "image", "test.jpeg", ContentType.TEXT_HTML.toString(), "Spring Framework".getBytes());
    requestDto.setTitle("");
    requestDto.setSummary("");
    requestDto.setDescription("");
    requestDto.setCategory("");
    requestDto.setImage(multipartFile);
    return requestDto;
  }

  public NewsUpdateRequestDto updateRequestDto() {
    NewsUpdateRequestDto requestDto = new NewsUpdateRequestDto();

    MockMultipartFile multipartFile =
        new MockMultipartFile("image", "test.jpeg", "image/jpeg", "Spring Framework".getBytes());

    requestDto.setTitle("Summer Sales");
    requestDto.setSummary("Steam Summer Sale 2022 continues — save big on top rated PC games");
    requestDto.setDescription(
        "Steam Summer Sale 2022 continues — save big on top rated PC gamesSteam "
            + "Summer Sale 2022 continues — save big on top rated PC gamesSteam "
            + "Summer Sale 2022 continues — save big on top rated PC games");
    requestDto.setCategory("OPINION");
    requestDto.setImage(multipartFile);
    return requestDto;
  }

  public NewsUpdateRequestDto updateRequestDtoWithInvalidFields() {
    NewsUpdateRequestDto requestDto = new NewsUpdateRequestDto();

    MockMultipartFile multipartFile =
        new MockMultipartFile("image", "test.jpeg", "text/jpeg", "Spring Framework".getBytes());

    requestDto.setTitle("");
    requestDto.setSummary("Steam Summer Sale 2022 continues — save big on top rated PC games");
    requestDto.setDescription(
        "Steam Summer Sale 2022 continues — save big on top rated PC gamesSteam "
            + "Summer Sale 2022 continues — save big on top rated PC gamesSteam "
            + "Summer Sale 2022 continues — save big on top rated PC games");
    requestDto.setCategory("OPINION");
    requestDto.setImage(multipartFile);
    return requestDto;
  }
}
