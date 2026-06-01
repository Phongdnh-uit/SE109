package com.uit.se109.services;

import com.uit.se109.dto.PageResponse;
import com.uit.se109.dto.province.ProvinceRequest;
import com.uit.se109.dto.province.ProvinceResponse;
import com.uit.se109.dto.province.ProvinceSummary;
import com.uit.se109.entities.Province;
import com.uit.se109.mappers.ProvinceMapper;
import com.uit.se109.repositories.ProvinceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProvinceServiceImpl implements ProvinceService {
  private final ProvinceRepository repository;
  private final ProvinceMapper mapper;

  @Override
  public PageResponse<ProvinceSummary> findAll(
      Pageable pageable, Specification<Province> specification) {
    return defaultFindAll(pageable, specification, mapper, repository);
  }

  @Override
  public ProvinceResponse findById(Long id) {
    return defaultFindById(id, mapper, repository);
  }

  @Override
  public ProvinceResponse create(ProvinceRequest input) {
    return defaultCreate(input, mapper, repository);
  }

  @Override
  public ProvinceResponse update(Long id, ProvinceRequest input) {
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
