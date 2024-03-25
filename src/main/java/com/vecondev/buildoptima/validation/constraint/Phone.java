package com.vecondev.buildoptima.validation.constraint;

import com.vecondev.buildoptima.validation.validator.PhoneValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Phone {

  String message() default """
      Invalid phone number! Should contain one '+' character 
      in the start and then 10 up to 14 numbers!""";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
