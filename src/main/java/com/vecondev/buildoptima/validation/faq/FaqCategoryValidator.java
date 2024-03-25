package com.vecondev.buildoptima.validation.faq;

import static com.vecondev.buildoptima.exception.Error.FAQ_CATEGORY_ALREADY_EXIST;

import com.vecondev.buildoptima.exception.FaqCategoryAlreadyExistException;
import com.vecondev.buildoptima.repository.faq.FaqCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FaqCategoryValidator {

  private final FaqCategoryRepository repository;

  public void validateCategoryName(String name) {
    if (Boolean.TRUE.equals(repository.existsByNameIgnoreCase(name))) {
      log.warn("Invalid name! There is a FAQ Category in database with such name.");

      throw new FaqCategoryAlreadyExistException(FAQ_CATEGORY_ALREADY_EXIST);
    }
  }
}
