package com.vecondev.buildoptima.model.property;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bo_prop_address")
public class Address implements Serializable {

  @Serial private static final long serialVersionUID = 9126326564896525001L;

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  private Property property;

  @Column(name = "house_number")
  private String houseNumber;

  @Column(name = "fraction")
  private String fraction;

  @Column(name = "direction")
  private String direction;

  @Column(name = "street_name")
  private String streetName;

  @Column(name = "street_suffix")
  private String streetSuffix;

  @Column(name = "street_suffix_direction")
  private String streetSuffixDirection;

  @Column(name = "unit")
  private String unit;

  @Column(name = "city")
  private String city;

  @Column(name = "state")
  private String state;

  @Column(name = "zip")
  private String zip;

  @Column(name = "is_primary")
  private boolean isPrimary;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Address address = (Address) o;
    return id != null && Objects.equals(id, address.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
