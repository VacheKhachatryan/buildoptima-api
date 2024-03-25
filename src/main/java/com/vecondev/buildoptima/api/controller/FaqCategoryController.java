package com.vecondev.buildoptima.api.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.vecondev.buildoptima.api.FaqCategoryApi;
import com.vecondev.buildoptima.dto.Metadata;
import com.vecondev.buildoptima.dto.faq.request.FaqCategoryRequestDto;
import com.vecondev.buildoptima.dto.faq.response.FaqCategoryResponseDto;
import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.dto.filter.FetchResponseDto;
import com.vecondev.buildoptima.service.auth.SecurityContextService;
import com.vecondev.buildoptima.service.faq.FaqCategoryService;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/faq/categories")
@PreAuthorize("hasAuthority('resource_write')")
public class FaqCategoryController implements FaqCategoryApi {

  private final FaqCategoryService faqCategoryService;
  private final SecurityContextService securityContextService;

  @Override
  @PostMapping
  public ResponseEntity<FaqCategoryResponseDto> create(
      @Valid @RequestBody FaqCategoryRequestDto requestDto) {
    UUID userId = securityContextService.getUserDetails().getId();
    log.info(
        "Attempt to create new FAQ Category with name: {} by user with id: {}",
        requestDto.getName(),
        userId);

    return new ResponseEntity<>(faqCategoryService.create(requestDto, userId), CREATED);
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<FaqCategoryResponseDto> getById(@PathVariable UUID id) {
    UUID userId = securityContextService.getUserDetails().getId();
    log.info("Retrieving the FAQ Category with id: {} by user with id: {}", id, userId);

    return ResponseEntity.ok(faqCategoryService.getById(id));
  }

  @Override
  @GetMapping
  public ResponseEntity<List<FaqCategoryResponseDto>> getAll() {
    UUID userId = securityContextService.getUserDetails().getId();
    log.info("Retrieving all FAQ Categories by user with id: {}", userId);

    return new ResponseEntity<>(faqCategoryService.getAll(), OK);
  }

  @Override
  @PostMapping("/fetch")
  public ResponseEntity<FetchResponseDto> fetch(@RequestBody FetchRequestDto fetchRequest) {
    UUID userId = securityContextService.getUserDetails().getId();
    log.info("User with id: {} is fetching faq questions.", userId);

    return ResponseEntity.ok(faqCategoryService.fetch(fetchRequest));
  }

  @Override
  @PutMapping("/{id}")
  public ResponseEntity<FaqCategoryResponseDto> update(
      @PathVariable UUID id, @Valid @RequestBody FaqCategoryRequestDto requestDto) {
    UUID userId = securityContextService.getUserDetails().getId();
    log.info("Attempt to update the FAQ Category with id: {} by user with id: {}", id, userId);

    return ResponseEntity.ok(faqCategoryService.update(id, requestDto, userId));
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    UUID userId = securityContextService.getUserDetails().getId();
    log.info("Attempt to delete the FAQ Category with id: {} by user with id: {}", id, userId);

    faqCategoryService.delete(id, userId);
    return new ResponseEntity<>(OK);
  }

  @Override
  @GetMapping("/csv")
  public ResponseEntity<Resource> exportInCsv() {
    UUID userId = securityContextService.getUserDetails().getId();
    log.info("User with id: {} trying to export all faq categories in '.csv' format.", userId);

    return faqCategoryService.exportInCsv();
  }

  @Override
  @GetMapping("/metadata")
  public ResponseEntity<Metadata> getMetadata() {
    UUID userId = securityContextService.getUserDetails().getId();
    log.info("User with id: {} trying to get the FAQ category metadata.", userId);

    return ResponseEntity.ok(faqCategoryService.getMetadata());
  }
}
