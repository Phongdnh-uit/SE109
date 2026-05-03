package com.uit.se109.services;

import com.uit.se109.dto.PageResponse;
import com.uit.se109.dto.ward.WardRequest;
import com.uit.se109.dto.ward.WardResponse;
import com.uit.se109.dto.ward.WardSummary;
import com.uit.se109.entities.Ward;
import com.uit.se109.mappers.WardMapper;
import com.uit.se109.repositories.WardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WardServiceImpl implements WardService {
  private final WardRepository repository;
  private final WardMapper mapper;

  @Override
  public PageResponse<WardSummary> findAll(Pageable pageable, Specification<Ward> specification) {
    return defaultFindAll(pageable, specification, mapper, repository);
  }

  @Override
  public WardResponse findById(Long id) {
    return defaultFindById(id, mapper, repository);
  }

  @Override
  public WardResponse create(WardRequest input) {
    return defaultCreate(input, mapper, repository);
  }

  @Override
  public WardResponse update(Long id, WardRequest input) {
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
