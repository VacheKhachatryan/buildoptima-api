package com.vecondev.buildoptima.mapper.property;

import static org.apache.commons.lang3.StringUtils.SPACE;

import com.vecondev.buildoptima.dto.property.AddressDto;
import com.vecondev.buildoptima.model.property.Address;
import com.vecondev.buildoptima.model.property.AddressDocument;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class AddressMapperDecorator implements AddressMapper {

  @Autowired
  @Qualifier("delegate")
  private AddressMapper mapper;

  @Override
  public AddressDto mapToDto(Address address) {
    AddressDto addressDto = mapper.mapToDto(address);
    addressDto.setPrimary(address.isPrimary());
    return addressDto;
  }

  @Override
  public List<AddressDto> mapToDtoList(List<Address> addresses) {
    List<AddressDto> addressDtos = new ArrayList<>();
    addresses.forEach(address -> addressDtos.add(mapToDto(address)));
    return addressDtos;
  }

  @Override
  public AddressDocument mapToDocument(Address address) {
    String searchable = getAsSearchableAddress(address);
    return AddressDocument.builder()
        .propertyAin(address.getProperty().getAin())
        .addressToSearch(searchable.toLowerCase())
        .addressToDisplay(searchable)
        .build();
  }

  @Override
  public List<AddressDocument> mapToDocumentList(List<Address> addresses) {
    return addresses.stream().map(this::mapToDocument).toList();
  }

  private String getAsSearchableAddress(Address address) {
    String searchable =
        String.join(
            SPACE,
            address.getHouseNumber(),
            address.getFraction(),
            address.getDirection(),
            address.getStreetName(),
            address.getStreetSuffix(),
            address.getStreetSuffixDirection(),
            address.getUnit(),
            address.getCity(),
            address.getState(),
            address.getZip());
    searchable = searchable.replaceAll("null", SPACE).replaceAll("\\s+", SPACE);
    return searchable;
  }
}
