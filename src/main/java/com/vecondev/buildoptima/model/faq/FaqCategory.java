package com.vecondev.buildoptima.model.faq;

import com.vecondev.buildoptima.model.AbstractEntity;
import com.vecondev.buildoptima.model.user.User;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "faq_category")
public class FaqCategory extends AbstractEntity {

  @Column(name = "name")
  private String name;

  @ManyToOne private User createdBy;

  @ManyToOne private User updatedBy;

  @Builder(toBuilder = true)
  public FaqCategory(
      UUID id, String name, User createdBy, User updatedBy, Instant createdAt, Instant updatedAt) {
    super(id, createdAt, updatedAt);
    this.name = name;
    this.createdBy = createdBy;
    this.updatedBy = updatedBy;
  }
}
