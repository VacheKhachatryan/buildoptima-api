package com.vecondev.buildoptima.mapper.news;

import com.vecondev.buildoptima.csv.news.NewsRecord;
import com.vecondev.buildoptima.dto.news.request.NewsCreateRequestDto;
import com.vecondev.buildoptima.dto.news.response.NewsResponseDto;
import com.vecondev.buildoptima.model.news.News;
import com.vecondev.buildoptima.model.user.User;
import java.util.List;
import org.mapstruct.Context;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(NewsMapperDecorator.class)
public interface NewsMapper {

  @Mapping(target = "keywords", ignore = true)
  News mapToEntity(NewsCreateRequestDto dto, @Context User createdBy);

  @Mapping(target = "keywords", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  NewsResponseDto mapToResponseDto(News news);

  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  NewsRecord mapToRecord(News news);

  @Mapping(target = "keywords", ignore = true)
  List<NewsResponseDto> mapToResponseList(Page<News> newsPage);

  List<NewsRecord> mapToNewsRecordList(List<News> news);
}
