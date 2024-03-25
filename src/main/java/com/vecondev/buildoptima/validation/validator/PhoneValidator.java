package com.vecondev.buildoptima.validation.validator;

import com.vecondev.buildoptima.validation.constraint.Phone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    if (value == null) {
      return true;
    }

    Matcher matcher = Pattern.compile("^[+]\\d{10,14}$").matcher(value);

    return matcher.matches();
  }

  @Override
  public void initialize(Phone constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }
}
