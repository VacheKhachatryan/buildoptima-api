package com.vecondev.buildoptima.csv.faq;

import com.vecondev.buildoptima.csv.CsvRecord;
import com.vecondev.buildoptima.csv.Header;
import com.vecondev.buildoptima.model.Status;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class FaqQuestionRecord implements CsvRecord {

  @Header("ID")
  private UUID id;

  @Header("Question")
  private String question;

  @Header("Answer")
  private String answer;

  @Header("Status")
  private Status status;

  @Header("Category")
  private String category;

  @Header("Created by")
  private String createdBy;

  @Header("Created at")
  private Instant createdAt;

  @Header("Updated by")
  private String updatedBy;

  @Header("Created at")
  private Instant updatedAt;

  @Override
  public List<Object> getAllFieldValues() {
    return List.of(
        id, question, answer, status, category, createdBy, createdAt, updatedBy, updatedAt);
  }
}
