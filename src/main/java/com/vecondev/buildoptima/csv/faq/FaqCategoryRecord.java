package com.vecondev.buildoptima.csv.faq;

import com.vecondev.buildoptima.csv.CsvRecord;
import com.vecondev.buildoptima.csv.Header;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class FaqCategoryRecord implements CsvRecord {

  @Header("ID")
  private UUID id;

  @Header("Name")
  private String name;

  @Header("Created by")
  private String createdBy;

  @Header("Created at")
  private Instant createdAt;

  @Header("Updated by")
  private String updatedBy;

  @Header("Updated at")
  private Instant updatedAt;

  @Override
  public List<Object> getAllFieldValues() {
    return List.of(id, name, createdBy, createdAt, updatedBy, updatedAt);
  }
}
