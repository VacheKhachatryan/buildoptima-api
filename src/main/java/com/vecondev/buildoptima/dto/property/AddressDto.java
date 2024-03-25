package com.vecondev.buildoptima.dto.property;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto implements Serializable {

  @Serial private static final long serialVersionUID = 5231439161518415907L;

  private String houseNumber;

  private String fraction;

  private String direction;

  private String streetName;

  private String streetSuffix;

  private String streetSuffixDirection;

  private String unit;

  private String city;

  private String state;

  private String zip;

  private boolean isPrimary;
}
