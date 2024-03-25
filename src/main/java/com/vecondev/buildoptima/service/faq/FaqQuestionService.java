package com.vecondev.buildoptima.service.faq;

import com.vecondev.buildoptima.dto.EntityOverview;
import com.vecondev.buildoptima.dto.Metadata;
import com.vecondev.buildoptima.dto.faq.request.FaqQuestionRequestDto;
import com.vecondev.buildoptima.dto.faq.response.FaqQuestionResponseDto;
import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.dto.filter.FetchResponseDto;
import com.vecondev.buildoptima.filter.model.DictionaryField;
import com.vecondev.buildoptima.model.Status;
import com.vecondev.buildoptima.model.faq.FaqQuestion;
import java.util.List;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface FaqQuestionService {

  List<FaqQuestionResponseDto> getAll();

  FaqQuestionResponseDto getById(UUID questionId);

  FaqQuestionResponseDto create(FaqQuestionRequestDto requestDto, UUID userId);

  FaqQuestionResponseDto update(UUID questionId, FaqQuestionRequestDto requestDto, UUID userId);

  void delete(UUID questionId, UUID userId);

  FaqQuestion findQuestionById(UUID questionId);

  FetchResponseDto fetch(FetchRequestDto fetchRequest);

  ResponseEntity<Resource> exportInCsv();

  Metadata getMetadata();

  List<EntityOverview> lookup(Status status, DictionaryField dictionary);
}
