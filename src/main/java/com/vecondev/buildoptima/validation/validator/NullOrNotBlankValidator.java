package com.vecondev.buildoptima.validation.validator;

import com.vecondev.buildoptima.validation.constraint.NullOrNotBlank;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlank, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value == null || !value.isEmpty();
  }
}
