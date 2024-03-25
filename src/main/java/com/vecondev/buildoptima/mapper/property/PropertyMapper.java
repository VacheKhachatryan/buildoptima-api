package com.vecondev.buildoptima.mapper.property;

import com.vecondev.buildoptima.dto.property.PropertyReadDto;
import com.vecondev.buildoptima.dto.property.response.PropertyResponseDto;
import com.vecondev.buildoptima.model.property.Property;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(PropertyMapperDecorator.class)
public interface PropertyMapper {

  @Mapping(target = "locations", ignore = true)
  Property mapToEntity(PropertyReadDto dto);

  @Mapping(target = "addresses", ignore = true)
  PropertyResponseDto mapToResponseDto(Property property);
}
