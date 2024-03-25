package com.vecondev.buildoptima.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.vecondev.buildoptima.csv.faq.FaqCategoryRecord;
import com.vecondev.buildoptima.dto.Metadata;
import com.vecondev.buildoptima.dto.faq.request.FaqCategoryRequestDto;
import com.vecondev.buildoptima.dto.faq.response.FaqCategoryResponseDto;
import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.dto.filter.FetchResponseDto;
import com.vecondev.buildoptima.exception.ConvertingFailedException;
import com.vecondev.buildoptima.exception.FaqCategoryNotFoundException;
import com.vecondev.buildoptima.exception.ResourceNotFoundException;
import com.vecondev.buildoptima.filter.converter.PageableConverter;
import com.vecondev.buildoptima.mapper.faq.FaqCategoryMapper;
import com.vecondev.buildoptima.model.faq.FaqCategory;
import com.vecondev.buildoptima.model.user.User;
import com.vecondev.buildoptima.parameters.faq.category.FaqCategoryServiceTestParameters;
import com.vecondev.buildoptima.repository.faq.FaqCategoryRepository;
import com.vecondev.buildoptima.service.csv.CsvServiceImpl;
import com.vecondev.buildoptima.service.faq.impl.FaqCategoryServiceImpl;
import com.vecondev.buildoptima.service.user.UserServiceImpl;
import com.vecondev.buildoptima.validation.faq.FaqCategoryValidator;
import com.vecondev.buildoptima.validation.validator.FieldNameValidator;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class FaqCategoryServiceTest {

  private final FaqCategoryServiceTestParameters testParameters =
      new FaqCategoryServiceTestParameters();

  @InjectMocks @Spy private FaqCategoryServiceImpl faqCategoryService;

  @Mock private FaqCategoryMapper faqCategoryMapper;
  @Mock private FaqCategoryRepository faqCategoryRepository;
  @Mock private FaqCategoryValidator faqCategoryValidator;
  @Mock private UserServiceImpl userService;
  @Mock private PageableConverter pageableConverter;
  @Mock private CsvServiceImpl<FaqCategoryRecord> csvService;

  @Test
  void successfulRetrievalOfAllCategories() {
    faqCategoryService.getAll();

    verify(faqCategoryRepository).findAll();
    verify(faqCategoryMapper).mapToListDto(any());
  }

  @Test
  void successfulRetrievalOfCategoryById() {
    doReturn(null).when(faqCategoryService).findCategoryById(any());
    faqCategoryService.getById(UUID.randomUUID());

    verify(faqCategoryMapper).mapToDto(any());
  }

  @Test
  void failedRetrievalOfCategoryByIdAsNotFound() {
    UUID id = UUID.randomUUID();

    doThrow(ResourceNotFoundException.class).when(faqCategoryService).findCategoryById(id);

    assertThrows(ResourceNotFoundException.class, () -> faqCategoryService.getById(id));
  }

  @Test
  void successfulCategoryCreation() {
    UUID userId = UUID.randomUUID();
    User user = testParameters.getUserById(userId);
    FaqCategoryRequestDto faqCategoryRequestDto = testParameters.getFaqCategoryRequestDto();
    FaqCategory faqCategory = testParameters.getFaqCategory(userId);

    when(userService.findUserById(userId)).thenReturn(user);
    when(faqCategoryMapper.mapToEntity(faqCategoryRequestDto, user)).thenReturn(faqCategory);
    faqCategory.setId(UUID.randomUUID());
    when(faqCategoryRepository.saveAndFlush(faqCategory)).thenReturn(faqCategory);
    when(faqCategoryMapper.mapToDto(faqCategory))
        .thenReturn(testParameters.getFaqCategoryResponseDto(faqCategory));

    FaqCategoryResponseDto methodResponse =
        faqCategoryService.create(faqCategoryRequestDto, userId);
    assertEquals(faqCategory.getName(), methodResponse.getName());
    assertEquals(
        String.format("%s %s", user.getFirstName(), user.getLastName()),
        methodResponse.getUpdatedBy().getName());
    verify(faqCategoryValidator).validateCategoryName(faqCategory.getName());
    verify(faqCategoryMapper).mapToDto(faqCategory);
  }

  @Test
  void failedCategoryCreationAsNameIsInvalid() {
    UUID userId = UUID.randomUUID();
    User user = testParameters.getUserById(userId);
    FaqCategoryRequestDto faqCategoryRequestDto = testParameters.getFaqCategoryRequestDto();
    FaqCategory faqCategory = testParameters.getFaqCategory(userId);

    when(userService.findUserById(userId)).thenReturn(user);
    when(faqCategoryMapper.mapToEntity(faqCategoryRequestDto, user)).thenReturn(faqCategory);
    doThrow(IllegalArgumentException.class)
        .when(faqCategoryValidator)
        .validateCategoryName(faqCategory.getName());

    assertThrows(
        IllegalArgumentException.class,
        () -> faqCategoryService.create(faqCategoryRequestDto, userId));
    verify(faqCategoryValidator).validateCategoryName(faqCategory.getName());
  }

  @Test
  void successfulCategoryUpdate() {
    String newName = "Properties";
    UUID userId = UUID.randomUUID();
    FaqCategoryRequestDto faqCategoryRequestDto = testParameters.getFaqCategoryRequestDto();
    faqCategoryRequestDto.setName(newName);
    FaqCategory faqCategory = testParameters.getFaqCategory(userId);
    FaqCategoryResponseDto faqCategoryResponseDto =
        testParameters.getFaqCategoryResponseDto(faqCategory);
    faqCategoryResponseDto.setName(newName);

    when(faqCategoryRepository.findById(faqCategory.getId())).thenReturn(Optional.of(faqCategory));
    when(faqCategoryMapper.mapToDto(any())).thenReturn(faqCategoryResponseDto);

    FaqCategoryResponseDto methodResponse =
        faqCategoryService.update(faqCategory.getId(), faqCategoryRequestDto, userId);
    assertEquals(newName, methodResponse.getName());
    assertEquals(faqCategory.getId(), methodResponse.getId());
    verify(faqCategoryValidator).validateCategoryName(faqCategoryRequestDto.getName());
    verify(faqCategoryRepository).saveAndFlush(any());
  }

  @Test
  void failedCategoryUpdateAsCategoryNotFound() {
    String newName = "Properties";
    UUID userId = UUID.randomUUID();
    FaqCategoryRequestDto faqCategoryRequestDto = testParameters.getFaqCategoryRequestDto();
    faqCategoryRequestDto.setName(newName);
    FaqCategory faqCategory = testParameters.getFaqCategory(userId);
    UUID categoryId = faqCategory.getId();

    when(faqCategoryRepository.findById(categoryId)).thenReturn(Optional.empty());

    assertThrows(
        FaqCategoryNotFoundException.class,
        () -> faqCategoryService.update(categoryId, faqCategoryRequestDto, userId));
  }

  @Test
  void successfulCategoryDeletion() {
    UUID userId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();

    when(faqCategoryRepository.existsById(categoryId)).thenReturn(true);

    faqCategoryService.delete(categoryId, userId);
    verify(faqCategoryRepository).deleteById(categoryId);
  }

  @Test
  void failedCategoryDeletionAsCategoryNotFound() {
    UUID userId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();

    when(faqCategoryRepository.existsById(categoryId)).thenReturn(false);

    assertThrows(
        FaqCategoryNotFoundException.class, () -> faqCategoryService.delete(categoryId, userId));
  }

  @Test
  void successfulFindingCategoryById() {
    UUID categoryId = UUID.randomUUID();

    when(faqCategoryRepository.findById(categoryId)).thenReturn(Optional.of(new FaqCategory()));

    faqCategoryService.findCategoryById(categoryId);
    verify(faqCategoryRepository).findById(categoryId);
  }

  @Test
  void failedFindingCategoryByIdAsCategoryNotFound() {
    UUID categoryId = UUID.randomUUID();

    when(faqCategoryRepository.findById(categoryId)).thenReturn(Optional.empty());

    assertThrows(
        FaqCategoryNotFoundException.class, () -> faqCategoryService.findCategoryById(categoryId));
  }

  @Test
  void successfulFetchingOfFaqCategories() {
    FetchRequestDto requestDto = testParameters.getFetchRequest();
    Pageable pageable = testParameters.getPageable(requestDto);
    Page<FaqCategory> result = new PageImpl<>(testParameters.getFaqCategoryList());

    try (MockedStatic<FieldNameValidator> validator =
        Mockito.mockStatic(FieldNameValidator.class)) {
      validator
          .when(() -> FieldNameValidator.validateFieldNames(any(), any()))
          .thenAnswer((Answer<Void>) invocation -> null);
    }
    when(pageableConverter.convert(requestDto)).thenReturn(pageable);
    when(faqCategoryRepository.findAll(any(Specification.class), any(Pageable.class)))
        .thenReturn(result);
    when(faqCategoryMapper.mapToListDtoFromPage(result))
        .thenReturn(testParameters.getFaqCategoryResponseDtoList(result.stream().toList()));

    FetchResponseDto responseDto = faqCategoryService.fetch(requestDto);
    assertEquals(result.getTotalElements(), responseDto.getTotalElements());
  }

  @Test
  void successfulFetchingOfFaqCategoriesWithDefaultSortDirectory() {
    FetchRequestDto requestDto = testParameters.getFetchRequest();
    requestDto.setSort(null);
    Pageable pageable = testParameters.getPageable(requestDto);
    Page<FaqCategory> result = new PageImpl<>(testParameters.getFaqCategoryList());

    try (MockedStatic<FieldNameValidator> validator =
        Mockito.mockStatic(FieldNameValidator.class)) {
      validator
          .when(() -> FieldNameValidator.validateFieldNames(any(), any()))
          .thenAnswer((Answer<Void>) invocation -> null);
    }
    when(pageableConverter.convert(requestDto)).thenReturn(pageable);
    when(faqCategoryRepository.findAll(any(Specification.class), any(Pageable.class)))
        .thenReturn(result);
    when(faqCategoryMapper.mapToListDtoFromPage(result))
        .thenReturn(testParameters.getFaqCategoryResponseDtoList(result.stream().toList()));

    FetchResponseDto responseDto = faqCategoryService.fetch(requestDto);
    assertEquals(result.getTotalElements(), responseDto.getTotalElements());
  }

  @Test
  void successfulExportingOfCategoriesInCsv() {
    List<FaqCategoryRecord> categoryRecords = testParameters.getFaqCategoryRecordList();

    when(faqCategoryMapper.mapToRecordList(any())).thenReturn(categoryRecords);
    when(csvService.writeToCsv(categoryRecords, FaqCategoryRecord.class))
        .thenReturn(new ByteArrayInputStream(new byte[] {}));

    ResponseEntity<Resource> response = faqCategoryService.exportInCsv();
    assertEquals(200, response.getStatusCodeValue());
    assertEquals(
        "application/csv",
        Objects.requireNonNull(response.getHeaders().get("Content-type")).get(0));
    verify(faqCategoryRepository).findAll();
  }

  @Test
  void failedExportingOfCategoriesInCsvAsConvertionToCsvFailed() {
    List<FaqCategoryRecord> categoryRecords = testParameters.getFaqCategoryRecordList();

    when(faqCategoryMapper.mapToRecordList(any())).thenReturn(categoryRecords);
    doThrow(ConvertingFailedException.class)
        .when(csvService)
        .writeToCsv(categoryRecords, FaqCategoryRecord.class);

    assertThrows(ConvertingFailedException.class, () -> faqCategoryService.exportInCsv());
    verify(faqCategoryRepository).findAll();
  }

  @Test
  void successfulGettingMetadata() {
    when(faqCategoryRepository.findTopByOrderByUpdatedAtDesc())
        .thenReturn(Optional.of(new FaqCategory()));
    faqCategoryService.getMetadata();

    verify(faqCategoryMapper).getMetadata(any(), any(), any());
    verify(faqCategoryRepository).count();
  }

  @Test
  void successfulGettingMetadataWithZeroCount() {
    when(faqCategoryRepository.findTopByOrderByUpdatedAtDesc()).thenReturn(Optional.empty());
    Metadata metadata = faqCategoryService.getMetadata();

    assertNull(metadata.getAllActiveCount());
    assertNull(metadata.getLastUpdatedBy());
  }
}
