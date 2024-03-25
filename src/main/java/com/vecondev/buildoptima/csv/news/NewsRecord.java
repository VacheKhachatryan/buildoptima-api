package com.vecondev.buildoptima.csv.news;

import com.vecondev.buildoptima.csv.CsvRecord;
import com.vecondev.buildoptima.csv.Header;
import com.vecondev.buildoptima.model.news.NewsCategory;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class NewsRecord implements CsvRecord {

  @Header("ID")
  private UUID id;

  @Header("Title")
  private String title;

  @Header("Summary")
  private String summary;

  @Header("Description")
  private String description;

  @Header("Category")
  private NewsCategory category;

  @Header("Created By")
  private String createdBy;

  @Header("Updated By")
  private String updatedBy;

  @Header("Created At")
  private Instant createdAt;

  @Header("Updated At")
  private Instant updatedAt;

  @Override
  public List<Object> getAllFieldValues() {
    return List.of(id, title, summary, description, createdBy, updatedBy, createdAt, updatedAt);
  }
}
