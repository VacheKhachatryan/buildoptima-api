package com.vecondev.buildoptima.model.user;

import com.vecondev.buildoptima.model.AbstractEntity;
import java.time.Instant;
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
import org.hibernate.annotations.ColumnDefault;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends AbstractEntity {

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "phone")
  private String phone;

  @Column(name = "email")
  private String email;

  @Column(name = "password")
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  private Role role;

  @Column(name = "enabled")
  @ColumnDefault("false")
  private boolean enabled;

  @Column(name = "image_version")
  private Integer imageVersion;

  @Builder(toBuilder = true)
  public User(
      UUID id,
      Instant createdAt,
      Instant updatedAt,
      String firstName,
      String lastName,
      String phone,
      String email,
      String password,
      Role role,
      boolean enabled,
      Integer imageVersion) {
    super(id, createdAt, updatedAt);
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
    this.email = email;
    this.password = password;
    this.role = role;
    this.enabled = enabled;
    this.imageVersion = imageVersion;
  }

  public String getFullName() {
    return String.format("%s %s (ID:%s)", firstName, lastName, id);
  }
}
