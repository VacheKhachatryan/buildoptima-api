package com.vecondev.buildoptima.model.news;

import com.vecondev.buildoptima.model.AbstractEntity;
import com.vecondev.buildoptima.model.Status;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "news")
public class News extends AbstractEntity {

  @Column(name = "title")
  private String title;

  @Column(name = "summary")
  private String summary;

  @Column(name = "description")
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private Status status;

  @Enumerated(EnumType.STRING)
  @Column(name = "news_category")
  private NewsCategory category;

  @Column(name = "keywords")
  private String keywords;

  private UUID createdBy;

  private UUID updatedBy;

  private Integer imageVersion;
}
