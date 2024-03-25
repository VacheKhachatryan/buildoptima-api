package com.vecondev.buildoptima.validation.faq;

import static com.vecondev.buildoptima.exception.Error.FAQ_QUESTION_ALREADY_EXIST;

import com.vecondev.buildoptima.exception.FaqQuestionAlreadyExistException;
import com.vecondev.buildoptima.repository.faq.FaqQuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FaqQuestionValidator {

  private final FaqQuestionRepository repository;

  public void validateQuestion(String question) {
    if (Boolean.TRUE.equals(repository.existsByQuestionIgnoreCase(question))) {
      log.warn(
          "Invalid FAQ Question! There is a FAQ Question in database with such question content.");

      throw new FaqQuestionAlreadyExistException(FAQ_QUESTION_ALREADY_EXIST);
    }
  }
}
