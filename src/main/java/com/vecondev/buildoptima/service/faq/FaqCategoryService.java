package com.vecondev.buildoptima.service.faq;

import com.vecondev.buildoptima.dto.Metadata;
import com.vecondev.buildoptima.dto.faq.request.FaqCategoryRequestDto;
import com.vecondev.buildoptima.dto.faq.response.FaqCategoryResponseDto;
import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.dto.filter.FetchResponseDto;
import com.vecondev.buildoptima.model.faq.FaqCategory;
import java.util.List;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface FaqCategoryService {

  List<FaqCategoryResponseDto> getAll();

  FaqCategoryResponseDto getById(UUID categoryId);

  FaqCategoryResponseDto create(FaqCategoryRequestDto requestDto, UUID userId);

  FaqCategoryResponseDto update(UUID categoryId, FaqCategoryRequestDto requestDto, UUID userId);

  void delete(UUID categoryId, UUID userId);

  FaqCategory findCategoryById(UUID id);

  FetchResponseDto fetch(FetchRequestDto fetchRequest);

  ResponseEntity<Resource> exportInCsv();

  Metadata getMetadata();
}
