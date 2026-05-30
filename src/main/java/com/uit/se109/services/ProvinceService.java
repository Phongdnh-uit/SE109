package com.uit.se109.services;

import com.uit.se109.dto.province.ProvinceRequest;
import com.uit.se109.dto.province.ProvinceResponse;
import com.uit.se109.dto.province.ProvinceSummary;
import com.uit.se109.entities.Province;

public interface ProvinceService
    extends CrudService<
        Province, Long, ProvinceRequest, ProvinceRequest, ProvinceResponse, ProvinceSummary> {}
