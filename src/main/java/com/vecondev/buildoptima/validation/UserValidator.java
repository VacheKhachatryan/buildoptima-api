package com.vecondev.buildoptima.validation;

import static com.vecondev.buildoptima.exception.Error.USER_ALREADY_EXIST_WITH_EMAIL;
import static com.vecondev.buildoptima.exception.Error.USER_ALREADY_EXIST_WITH_PHONE;

import com.vecondev.buildoptima.exception.UserAlreadyExistException;
import com.vecondev.buildoptima.model.user.User;
import com.vecondev.buildoptima.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
@Setter
public class UserValidator {

  private UserRepository userRepository;

  public void validateUserRegistration(User user) {
    validateEmail(user.getEmail());
    validatePhone(user.getPhone());
  }

  private void validateEmail(String email) {
    if (userRepository.existsByEmailIgnoreCase(email)) {
      log.warn("Invalid email! There is an user in database with such email.");

      throw new UserAlreadyExistException(USER_ALREADY_EXIST_WITH_EMAIL);
    }
  }

  private void validatePhone(String phone) {
    if (userRepository.existsByPhone(phone)) {
      log.warn("Invalid phone number! There is an user in database with such phone number.");

      throw new UserAlreadyExistException(USER_ALREADY_EXIST_WITH_PHONE);
    }
  }
}
