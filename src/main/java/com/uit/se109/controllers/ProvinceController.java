package com.uit.se109.controllers;

import com.uit.se109.dto.province.ProvinceRequest;
import com.uit.se109.dto.province.ProvinceResponse;
import com.uit.se109.dto.province.ProvinceSummary;
import com.uit.se109.entities.Province;
import com.uit.se109.services.CrudService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Province", description = "Endpoints for managing provinces")
@RequestMapping("/api/v1/provinces")
@RestController
public class ProvinceController
    extends GenericController<
        Province, Long, ProvinceRequest, ProvinceRequest, ProvinceResponse, ProvinceSummary> {

  public ProvinceController(
      CrudService<
              Province, Long, ProvinceRequest, ProvinceRequest, ProvinceResponse, ProvinceSummary>
          service) {
    super(service);
  }
}
