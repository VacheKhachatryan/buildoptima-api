package com.vecondev.buildoptima.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

  @Serial private static final long serialVersionUID = -3138146155196509702L;

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", updatable = false, nullable = false)
  protected UUID id;

  @CreationTimestamp
  @Column(name = "created_at")
  protected Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  protected Instant updatedAt;

  protected AbstractEntity(UUID id, Instant createdAt, Instant updatedAt) {
    this.id = id;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
}
