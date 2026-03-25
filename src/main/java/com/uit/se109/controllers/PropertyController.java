package com.uit.se109.controllers;

import com.uit.se109.dto.property.PropertyRequest;
import com.uit.se109.dto.property.PropertyResponse;
import com.uit.se109.dto.property.PropertySummary;
import com.uit.se109.entities.Property;
import com.uit.se109.services.CrudService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Property", description = "Endpoints for managing properties")
@RequestMapping("/properties")
@RestController
public class PropertyController
    extends GenericController<
        Property, Long, PropertyRequest, PropertyRequest, PropertyResponse, PropertySummary> {

  public PropertyController(
      CrudService<
              Property, Long, PropertyRequest, PropertyRequest, PropertyResponse, PropertySummary>
          service) {
    super(service);
  }
}
