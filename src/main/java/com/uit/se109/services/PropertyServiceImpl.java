package com.uit.se109.services;

import com.uit.se109.dto.PageResponse;
import com.uit.se109.dto.property.PropertyRequest;
import com.uit.se109.dto.property.PropertyResponse;
import com.uit.se109.dto.property.PropertySummary;
import com.uit.se109.entities.Property;
import com.uit.se109.mappers.PropertyMapper;
import com.uit.se109.repositories.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {
  private final PropertyRepository repository;
  private final PropertyMapper mapper;

  @Override
  public PageResponse<PropertySummary> findAll(
      Pageable pageable, Specification<Property> specification) {
    return defaultFindAll(pageable, specification, mapper, repository);
  }

  @Override
  public PropertyResponse findById(Long id) {
    return defaultFindById(id, mapper, repository);
  }

  @Override
  public PropertyResponse create(PropertyRequest input) {
    return defaultCreate(input, mapper, repository);
  }

  @Override
  public PropertyResponse update(Long id, PropertyRequest input) {
    return defaultUpdate(id, input, mapper, repository);
  }

  @Override
  public void delete(Long id) {
    defaultDelete(id, repository);
  }

  @Override
  public void deleteAll(Iterable<Long> ids) {
    defaultDeleteAll(ids, repository);
  }
}
