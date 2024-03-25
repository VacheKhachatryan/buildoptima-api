package com.vecondev.buildoptima.api.controller;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.vecondev.buildoptima.api.NewsApi;
import com.vecondev.buildoptima.dto.Metadata;
import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.dto.filter.FetchResponseDto;
import com.vecondev.buildoptima.dto.news.request.NewsCreateRequestDto;
import com.vecondev.buildoptima.dto.news.request.NewsUpdateRequestDto;
import com.vecondev.buildoptima.dto.news.response.NewsResponseDto;
import com.vecondev.buildoptima.service.news.NewsService;
import java.time.Instant;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
@PreAuthorize("hasAuthority('resource_write')")
public class NewsController implements NewsApi {

  private final NewsService newsService;

  @Override
  @PostMapping(consumes = {MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<NewsResponseDto> create(
      @ModelAttribute @Valid NewsCreateRequestDto requestDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(newsService.create(requestDto));
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<NewsResponseDto> getById(@PathVariable UUID id) {
    return ResponseEntity.ok(newsService.getById(id));
  }

  @Override
  @PostMapping("/fetch")
  public ResponseEntity<FetchResponseDto> fetch(@RequestBody FetchRequestDto fetchRequestDto) {
    return ResponseEntity.ok(newsService.fetch(fetchRequestDto));
  }

  @Override
  @PatchMapping(
      path = "/{id}",
      consumes = {MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<NewsResponseDto> update(
      @PathVariable("id") UUID id, @ModelAttribute @Valid NewsUpdateRequestDto requestDto) {
    return ResponseEntity.ok(newsService.update(id, requestDto));
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
    newsService.delete(id);
    return ResponseEntity.ok().build();
  }

  @Override
  @PostMapping("/csv")
  public ResponseEntity<Resource> exportInCsv(@RequestBody FetchRequestDto fetchRequestDto) {
    String csvFileName = String.format("news-%s.csv", Instant.now());
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType("application/csv"))
        .header("Content-Disposition", String.format("attachment; filename=\"%s\"", csvFileName))
        .body(newsService.exportCsv(fetchRequestDto));
  }

  @Override
  @GetMapping("/metadata")
  public ResponseEntity<Metadata> getMetadata() {
    return ResponseEntity.ok(newsService.getMetadata());
  }

  @Override
  @PatchMapping("/{id}/archive")
  public ResponseEntity<NewsResponseDto> archive(@PathVariable UUID id) {
    return ResponseEntity.ok(newsService.archiveNews(id));
  }
}
