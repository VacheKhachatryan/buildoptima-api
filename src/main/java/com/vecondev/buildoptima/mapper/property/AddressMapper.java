package com.vecondev.buildoptima.mapper.property;

import com.vecondev.buildoptima.dto.property.AddressDto;
import com.vecondev.buildoptima.model.property.Address;
import com.vecondev.buildoptima.model.property.AddressDocument;
import java.util.List;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(AddressMapperDecorator.class)
public interface AddressMapper {

  @Mapping(target = "isPrimary", ignore = true)
  AddressDto mapToDto(Address address);

  List<AddressDto> mapToDtoList(List<Address> addresses);

  AddressDocument mapToDocument(Address address);

  List<AddressDocument> mapToDocumentList(List<Address> addresses);
}
