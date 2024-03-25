package com.vecondev.buildoptima.validation.validator;

import com.vecondev.buildoptima.validation.constraint.Password;
import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

public class UserPasswordValidator implements ConstraintValidator<Password, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.length() == 0) {
      return true;
    }

    List<Rule> rules = new ArrayList<>();

    rules.add(new LengthRule(8, 32));
    rules.add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
    rules.add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
    rules.add(new CharacterRule(EnglishCharacterData.Digit, 1));
    rules.add(new CharacterRule(EnglishCharacterData.Special, 1));
    rules.add(new WhitespaceRule());
    PasswordValidator passwordValidator = new PasswordValidator(rules);
    RuleResult result = passwordValidator.validate(new PasswordData(value));

    return result.isValid();
  }

  @Override
  public void initialize(Password constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }
}
