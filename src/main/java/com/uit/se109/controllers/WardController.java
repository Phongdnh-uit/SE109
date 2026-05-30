package com.uit.se109.controllers;

import com.uit.se109.dto.ward.WardRequest;
import com.uit.se109.dto.ward.WardResponse;
import com.uit.se109.dto.ward.WardSummary;
import com.uit.se109.entities.Ward;
import com.uit.se109.services.CrudService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Ward", description = "Endpoints for managing wards")
@RequestMapping("/api/v1/wards")
@RestController
public class WardController
    extends GenericController<Ward, Long, WardRequest, WardRequest, WardResponse, WardSummary> {

  public WardController(
      CrudService<Ward, Long, WardRequest, WardRequest, WardResponse, WardSummary> service) {
    super(service);
  }
}
