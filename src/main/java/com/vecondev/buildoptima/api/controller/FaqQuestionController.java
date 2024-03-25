package com.vecondev.buildoptima.api.controller;

import static com.vecondev.buildoptima.filter.model.DictionaryField.UPDATED_BY;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.vecondev.buildoptima.api.FaqQuestionApi;
import com.vecondev.buildoptima.dto.EntityOverview;
import com.vecondev.buildoptima.dto.Metadata;
import com.vecondev.buildoptima.dto.faq.request.FaqQuestionRequestDto;
import com.vecondev.buildoptima.dto.faq.response.FaqQuestionResponseDto;
import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.dto.filter.FetchResponseDto;
import com.vecondev.buildoptima.filter.model.DictionaryField;
import com.vecondev.buildoptima.model.Status;
import com.vecondev.buildoptima.service.auth.SecurityContextService;
import com.vecondev.buildoptima.service.faq.FaqQuestionService;
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
@RequestMapping("/faq/questions")
@PreAuthorize("hasAuthority('resource_write')")
public class FaqQuestionController implements FaqQuestionApi {

  private final FaqQuestionService faqQuestionService;
  private final SecurityContextService securityContextService;

  @Override
  @PostMapping
  public ResponseEntity<FaqQuestionResponseDto> create(
      @Valid @RequestBody FaqQuestionRequestDto requestDto) {
    UUID userId = securityContextService.getUserDetails().getId();
    log.info("Attempt to create new FAQ Question by user with id: {}", userId);

    return new ResponseEntity<>(faqQuestionService.create(requestDto, userId), CREATED);
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<FaqQuestionResponseDto> getById(@PathVariable UUID id) {
    UUID userId = securityContextService.getUserDetails().getId();
    log.info("Retrieving the FAQ Question with id: {} by user with id: {}", id, userId);

    return ResponseEntity.ok(faqQuestionService.getById(id));
  }

  @Override
  @GetMapping
  public ResponseEntity<List<FaqQuestionResponseDto>> getAll() {
    UUID userId = securityContextService.getUserDetails().getId();
    log.info("Retrieving all FAQ Questions by user with id: {}", userId);

    return new ResponseEntity<>(faqQuestionService.getAll(), OK);
  }

  @Override
  @PostMapping("/fetch")
  public ResponseEntity<FetchResponseDto> fetch(@RequestBody FetchRequestDto fetchRequest) {
    UUID userId = securityContextService.getUserDetails().getId();
    log.info("User with id: {} is fetching faq categories.", userId);

    return ResponseEntity.ok(faqQuestionService.fetch(fetchRequest));
  }

  @Override
  @PutMapping("/{id}")
  public ResponseEntity<FaqQuestionResponseDto> update(
      @PathVariable UUID id, @Valid @RequestBody FaqQuestionRequestDto requestDto) {
    UUID userId = securityContextService.getUserDetails().getId();
    log.info("Attempt to update the FAQ Question with id: {} by user with id: {}", id, userId);

    return ResponseEntity.ok(faqQuestionService.update(id, requestDto, userId));
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    UUID userId = securityContextService.getUserDetails().getId();
    log.info("Attempt to delete the FAQ Category with id: {} by user with id: {}", id, userId);

    faqQuestionService.delete(id, userId);
    return new ResponseEntity<>(OK);
  }

  @Override
  @GetMapping(value = "/csv")
  public ResponseEntity<Resource> exportInCsv() {
    UUID userId = securityContextService.getUserDetails().getId();
    log.info("User with id: {} trying to export all faq questions in '.csv' format.", userId);

    return faqQuestionService.exportInCsv();
  }

  @Override
  @GetMapping(value = "/metadata")
  public ResponseEntity<Metadata> getMetadata() {
    UUID userId = securityContextService.getUserDetails().getId();
    log.info("User with id: {} trying to get the FAQ question metadata.", userId);

    return ResponseEntity.ok(faqQuestionService.getMetadata());
  }

  @Override
  @GetMapping("/lookup/{status}/{dictionary}")
  public ResponseEntity<List<EntityOverview>> lookup(
      @PathVariable Status status, @PathVariable DictionaryField dictionary) {
    UUID userId = securityContextService.getUserDetails().getId();
    log.info(
        "User with id: {} looking for {} that have questions those status is: {}.",
        userId,
        dictionary == UPDATED_BY ? "userS" : "categories",
        status);

    return ResponseEntity.ok(faqQuestionService.lookup(status, dictionary));
  }
}
