package com.vecondev.buildoptima.mapper.property;

import com.vecondev.buildoptima.dto.property.PropertyReadDto;
import com.vecondev.buildoptima.dto.property.response.PropertyResponseDto;
import com.vecondev.buildoptima.model.property.Address;
import com.vecondev.buildoptima.model.property.Locations;
import com.vecondev.buildoptima.model.property.Property;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class PropertyMapperDecorator implements PropertyMapper {

  @Autowired
  @Qualifier("delegate")
  private PropertyMapper mapper;

  @Autowired private AddressMapper addressMapper;

  @Override
  public Property mapToEntity(PropertyReadDto dto) {
    List<Address> addresses = dto.getAssociatedAddresses();
    Address primaryAddress = dto.getPropertyAddress();
    if (primaryAddress != null) {
      primaryAddress.setPrimary(true);
      addresses.add(primaryAddress);
    }
    Locations locations = new Locations(dto.getCentroid(), dto.getPolygons());

    Property property = mapper.mapToEntity(dto);
    property.addAddresses(addresses);
    property.setLocations(locations);
    return property;
  }

  @Override
  public PropertyResponseDto mapToResponseDto(Property property) {
    PropertyResponseDto responseDto = mapper.mapToResponseDto(property);
    responseDto.setAddresses(addressMapper.mapToDtoList(property.getAddresses()));
    return responseDto;
  }
}
