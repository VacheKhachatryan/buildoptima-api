package com.vecondev.buildoptima.dto.user.request;

import com.vecondev.buildoptima.validation.constraint.Name;
import com.vecondev.buildoptima.validation.constraint.Password;
import com.vecondev.buildoptima.validation.constraint.Phone;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class UserRegistrationRequestDto {

  @Name
  @Schema(
      title = "User's first name",
      description =
          "Name's length should be between 2 and 20 and name should contain only letters.",
      example = "John",
      required = true,
      minLength = 2,
      maxLength = 20,
      pattern = "^[A-Za-z]{2,20}$")
  private String firstName;

  @Name
  @Schema(
      title = "User's last name",
      description =
          "Lastname's length should be between 2 and 20 and lastname should contain only letters.",
      example = "Smith",
      required = true,
      minLength = 2,
      maxLength = 20,
      pattern = "^[A-Za-z]{2,20}$")
  private String lastName;

  @NotNull
  @Phone
  @Schema(
      title = "User's phone number",
      description =
          "Phone number should start with '+' character and have 10 up to 14 numbers after it.",
      example = "+37477123456",
      required = true,
      minLength = 11,
      maxLength = 15,
      pattern = "^[+]{1}[0-9]{10,14}$")
  private String phone;

  @NotNull
  @Email
  @Schema(
      title = "User's email",
      description = "Should have valid email format",
      example = "example@gmail.com",
      required = true)
  private String email;

  @NotNull
  @Password
  @Schema(
      title = "User's password",
      description =
          "User's password: should have valid password format, at least one uppercase character, "
              + "one lowercase character, one digit, one special symbol and no whitespaces!",
      example = "Example1234.",
      required = true,
      minLength = 8,
      maxLength = 32)
  private String password;
}
