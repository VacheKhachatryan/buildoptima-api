package com.vecondev.buildoptima.validation.constraint;

import com.vecondev.buildoptima.validation.validator.UserPasswordValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = UserPasswordValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {

  String message() default
          """
           Invalid password! The password should have 8 up to 32 characters at least one
           uppercase character, one lowercase character, one digit,
           one special symbol and no whitespaces!""";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
