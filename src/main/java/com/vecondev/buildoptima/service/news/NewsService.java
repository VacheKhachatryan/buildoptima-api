package com.vecondev.buildoptima.service.news;

import com.vecondev.buildoptima.dto.Metadata;
import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.dto.filter.FetchResponseDto;
import com.vecondev.buildoptima.dto.news.request.NewsCreateRequestDto;
import com.vecondev.buildoptima.dto.news.request.NewsUpdateRequestDto;
import com.vecondev.buildoptima.dto.news.response.NewsResponseDto;
import java.util.UUID;
import org.springframework.core.io.InputStreamResource;

public interface NewsService {

  NewsResponseDto create(NewsCreateRequestDto dto);

  NewsResponseDto update(UUID id, NewsUpdateRequestDto dto);

  void delete(UUID id);

  NewsResponseDto getById(UUID id);

  FetchResponseDto fetch(FetchRequestDto fetchRequestDto);

  Metadata getMetadata();

  InputStreamResource exportCsv(FetchRequestDto fetchRequestDto);

  NewsResponseDto archiveNews(UUID id);
}
