package com.vecondev.buildoptima.dto.property;

import com.vecondev.buildoptima.model.property.Address;
import com.vecondev.buildoptima.model.property.Centroid;
import com.vecondev.buildoptima.model.property.Details;
import com.vecondev.buildoptima.model.property.Hazards;
import com.vecondev.buildoptima.model.property.Polygon;
import com.vecondev.buildoptima.model.property.ZoningDetails;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PropertyReadDto {

  private String ain;
  private String municipality;
  private Address propertyAddress;
  private List<Address> associatedAddresses;
  private Centroid centroid;
  private Set<Polygon> polygons;
  private Details details;
  private Hazards hazards;
  private ZoningDetails zoningDetails;
}
