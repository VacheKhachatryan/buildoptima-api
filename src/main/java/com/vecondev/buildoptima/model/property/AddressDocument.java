package com.vecondev.buildoptima.model.property;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDocument {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @JsonProperty("property_ain")
  private String propertyAin;

  @JsonProperty("address_to_search")
  private String addressToSearch;

  @JsonProperty("address_to_display")
  private String addressToDisplay;

  public static Map<String, String> getAsMap(AddressDocument addressDocument) {
    return OBJECT_MAPPER.convertValue(addressDocument, new TypeReference<>() {});
  }
}
