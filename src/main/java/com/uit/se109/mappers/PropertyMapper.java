package com.uit.se109.mappers;

import com.uit.se109.dto.property.PropertyRequest;
import com.uit.se109.dto.property.PropertyResponse;
import com.uit.se109.dto.property.PropertySummary;
import com.uit.se109.entities.Property;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PropertyMapper
    extends GenericMapper<
        Property, PropertyRequest, PropertyRequest, PropertyResponse, PropertySummary> {

  @Override
  @Mapping(source = "wardId", target = "ward.id")
  Property requestToEntity(PropertyRequest request);

  @Override
  @Mapping(source = "ward.id", target = "wardId")
  PropertyResponse entityToResponse(Property entity);

  @Override
  @Mapping(source = "wardId", target = "ward.id")
  void partialUpdate(PropertyRequest request, @MappingTarget Property entity);
}
