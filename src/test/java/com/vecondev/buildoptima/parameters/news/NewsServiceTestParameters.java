package com.vecondev.buildoptima.parameters.news;

import static com.vecondev.buildoptima.filter.model.SearchOperation.GT;
import static com.vecondev.buildoptima.filter.model.SearchOperation.LIKE;

import com.vecondev.buildoptima.csv.news.NewsRecord;
import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.dto.news.request.NewsCreateRequestDto;
import com.vecondev.buildoptima.dto.news.request.NewsUpdateRequestDto;
import com.vecondev.buildoptima.dto.news.response.NewsResponseDto;
import com.vecondev.buildoptima.filter.model.Criteria;
import com.vecondev.buildoptima.filter.model.SortDto;
import com.vecondev.buildoptima.model.Status;
import com.vecondev.buildoptima.model.news.News;
import com.vecondev.buildoptima.model.news.NewsCategory;
import com.vecondev.buildoptima.model.user.User;
import com.vecondev.buildoptima.parameters.user.UserServiceTestParameters;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NewsServiceTestParameters {

  private final UserServiceTestParameters userServiceTestParameters =
      new UserServiceTestParameters();

  public NewsCreateRequestDto getCreateNewsRequestDto() {
    return NewsCreateRequestDto.builder()
        .title("Summer Sales")
        .summary("Steam Summer Sale 2022 continues — save big on top rated PC games")
        .description("stringstringstringstringstringstringstringstringst")
        .category("BREAKING_NEWS")
        .build();
  }

  public NewsUpdateRequestDto getUpdateNewsRequestDto() {
    return NewsUpdateRequestDto.builder()
        .title("Winter Sales")
        .summary("Steam Summer Sale 2022 continues — save big on top rated PC games")
        .description("stringstringstringstringstringstringstringstringst")
        .category("OPINION")
        .build();
  }

  public News getNewsFromCreateNewsDto(NewsCreateRequestDto dto) {
    return News.builder()
        .title(dto.getTitle())
        .summary(dto.getSummary())
        .description(dto.getDescription())
        .category(NewsCategory.valueOf(dto.getCategory()))
        .status(Status.ACTIVE)
        .build();
  }

  public NewsResponseDto getNewsResponseDto(News news) {
    return NewsResponseDto.builder()
        .id(news.getId())
        .title(news.getTitle())
        .summary(news.getSummary())
        .description(news.getDescription())
        .category(news.getCategory())
        .createdBy(
            userServiceTestParameters.getUserOverView(userServiceTestParameters.getSavedUser()))
        .updatedBy(
            userServiceTestParameters.getUserOverView(userServiceTestParameters.getSavedUser()))
        .createdAt(Instant.now())
        .updatedAt(news.getUpdatedAt())
        .build();
  }

  public NewsRecord getNewsRecord(News news) {
    return NewsRecord.builder()
        .id(news.getId())
        .title(news.getTitle())
        .summary(news.getSummary())
        .description(news.getDescription())
        .category(news.getCategory())
        .createdBy("Poxos poxosyan")
        .updatedBy("Petros Petrosyan")
        .updatedAt(Instant.now())
        .createdAt(Instant.now())
        .build();
  }

  public List<News> getFetchResponse() {
    return List.of(
        News.builder().title("Summer Sales").summary("Summer Sales").build(),
        News.builder().title("Winter Sales").summary("Winter Sales").build());
  }

  public List<NewsResponseDto> getNewsResponseDtoList(List<News> news) {
    return news.stream().map(this::getNewsResponseDto).collect(Collectors.toList());
  }

  public List<NewsRecord> getNewsRecordList(List<News> news) {
    return news.stream().map(this::getNewsRecord).collect(Collectors.toList());
  }

  public FetchRequestDto getFetchRequest() {
    return new FetchRequestDto(
        Map.of(
            "and",
            List.of(
                new Criteria(LIKE, "title", "Summer"),
                Map.of("or", List.of(new Criteria(GT, "createdAt", "2018-11-30T18:35:24.00Z"))))),
        0,
        10,
        List.of(new SortDto("title", SortDto.Direction.ASC)));
  }

  public News getSavedNews(NewsCreateRequestDto dto, User user) {
    News news =
        News.builder()
            .title(dto.getTitle())
            .summary(dto.getSummary())
            .description(dto.getDescription())
            .category(NewsCategory.valueOf(dto.getCategory()))
            .status(Status.ACTIVE)
            .build();
    news.setCreatedAt(Instant.now());
    news.setUpdatedAt(Instant.now());
    news.setCreatedBy(user.getId());
    news.setUpdatedBy(user.getId());
    return news;
  }
}
