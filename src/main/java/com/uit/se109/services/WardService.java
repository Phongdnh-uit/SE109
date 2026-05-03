package com.uit.se109.services;

import com.uit.se109.dto.ward.WardRequest;
import com.uit.se109.dto.ward.WardResponse;
import com.uit.se109.dto.ward.WardSummary;
import com.uit.se109.entities.Ward;

public interface WardService
    extends CrudService<Ward, Long, WardRequest, WardRequest, WardResponse, WardSummary> {}
