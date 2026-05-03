package com.uit.se109.mappers;

import com.uit.se109.dto.province.ProvinceRequest;
import com.uit.se109.dto.province.ProvinceResponse;
import com.uit.se109.dto.province.ProvinceSummary;
import com.uit.se109.entities.Province;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProvinceMapper
    extends GenericMapper<
        Province, ProvinceRequest, ProvinceRequest, ProvinceResponse, ProvinceSummary> {}
