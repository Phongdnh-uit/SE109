package com.uit.se109.mappers;

import com.uit.se109.dto.ward.WardRequest;
import com.uit.se109.dto.ward.WardResponse;
import com.uit.se109.dto.ward.WardSummary;
import com.uit.se109.entities.Ward;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WardMapper
    extends GenericMapper<Ward, WardRequest, WardRequest, WardResponse, WardSummary> {

  @Override
  @Mapping(source = "provinceId", target = "province.id")
  Ward requestToEntity(WardRequest request);

  @Override
  @Mapping(source = "province.id", target = "provinceId")
  WardResponse entityToResponse(Ward entity);

  @Override
  @Mapping(source = "provinceId", target = "province.id")
  void partialUpdate(WardRequest request, @MappingTarget Ward entity);
}
