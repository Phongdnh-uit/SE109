package com.uit.se109.services;

import com.uit.se109.dto.property.PropertyRequest;
import com.uit.se109.dto.property.PropertyResponse;
import com.uit.se109.dto.property.PropertySummary;
import com.uit.se109.entities.Property;

public interface PropertyService
    extends CrudService<
        Property, Long, PropertyRequest, PropertyRequest, PropertyResponse, PropertySummary> {}
