package com.uit.se109.controllers;

import com.uit.se109.dto.ApiResponse;
import com.uit.se109.dto.PageResponse;
import com.uit.se109.dto.property.PropertyRequest;
import com.uit.se109.dto.property.PropertyResponse;
import com.uit.se109.dto.property.PropertySummary;
import com.uit.se109.entities.Property;
import com.uit.se109.services.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Property", description = "Endpoints for managing properties")
@RequestMapping("/api/v1/properties")
@RestController
public class PropertyController
    extends GenericController<
        Property, Long, PropertyRequest, PropertyRequest, PropertyResponse, PropertySummary> {

  private final PropertyService propertyService;

  public PropertyController(PropertyService service) {
    super(service);
    this.propertyService = service;
  }

  @Operation(operationId = "findAllMyProperties")
  @GetMapping("/me")
  public ResponseEntity<ApiResponse<PageResponse<PropertySummary>>> findAllByMe(
      @ParameterObject Pageable pageable) {
    return ResponseEntity.ok(ApiResponse.ok(propertyService.findAllByMe(pageable)));
  }
}
