package com.vecondev.buildoptima.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationMessage {

  private String template;
  private String token;
  private String userEmail;
  private String userFirstName;

  @Override
  public String toString() {
    return String.format("""
        {
        "template": "%s",
        "token": "%s",
        "userEmail": "%s",
        "userFirstName": "%s"
        }
        """, template, token, userEmail, userFirstName);
  }
}