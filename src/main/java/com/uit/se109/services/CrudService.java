package com.uit.se109.services;

import com.uit.se109.dto.PageResponse;
import com.uit.se109.exception.AppException;
import com.uit.se109.exception.ErrorCode;
import com.uit.se109.mappers.GenericMapper;
import com.uit.se109.repositories.CommonRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface CrudService<E, ID, C, U, O, S> {

  PageResponse<S> findAll(Pageable pageable, Specification<E> specification);

  O findById(ID id);

  O create(C input);

  O update(ID id, U input);

  void delete(ID id);

  void deleteAll(Iterable<ID> ids);

  // ============================ FIND ALL ============================

  default PageResponse<S> defaultFindAll(
      Pageable pageable,
      Specification<E> specification,
      GenericMapper<E, C, U, O, S> mapper,
      CommonRepository<E, ID> repository) {
    PageResponse<S> response =
        PageResponse.fromPage(
            repository.findAll(specification, pageable).map(mapper::entityToSummaryResponse));
    afterFindAll(response);
    return response;
  }

  default void afterFindAll(PageResponse<S> response) {}

  // ============================ FIND BY ID ============================

  default O defaultFindById(
      ID id, GenericMapper<E, C, U, O, S> mapper, CommonRepository<E, ID> repository) {
    E entity =
        repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
    O response = mapper.entityToResponse(entity);
    afterFindById(response);
    return response;
  }

  default void afterFindById(O response) {}

  // ============================ CREATE ============================

  default O defaultCreate(
      C input, GenericMapper<E, C, U, O, S> mapper, CommonRepository<E, ID> repository) {
    Map<String, Object> context = new HashMap<>();
    beforeCreate(input, context);
    E entity = mapper.requestToEntity(input);
    enrichCreate(input, entity, context);
    E savedEntity = repository.save(entity);
    O response = mapper.entityToResponse(savedEntity);
    afterCreate(savedEntity, response, context);
    return response;
  }

  default void beforeCreate(C input, Map<String, Object> context) {}

  default void enrichCreate(C input, E entity, Map<String, Object> context) {}

  default void afterCreate(E entity, O response, Map<String, Object> context) {}

  // ============================ UPDATE ============================

  default O defaultUpdate(
      ID id, U input, GenericMapper<E, C, U, O, S> mapper, CommonRepository<E, ID> repository) {
    E entity =
        repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
    Map<String, Object> context = new HashMap<>();
    beforeUpdate(id, input, entity, context);
    mapper.partialUpdate(input, entity);
    enrichUpdate(id, input, entity, context);
    entity = repository.save(entity);
    O response = mapper.entityToResponse(entity);
    afterUpdate(entity, response, context);
    return response;
  }

  default void beforeUpdate(ID id, U input, E entity, Map<String, Object> context) {}

  default void enrichUpdate(ID id, U input, E entity, Map<String, Object> context) {}

  default void afterUpdate(E entity, O response, Map<String, Object> context) {}

  // ============================ DELETE BY ID ============================

  default void defaultDelete(ID id, CommonRepository<E, ID> repository) {
    Map<String, Object> context = new HashMap<>();
    beforeDelete(id, context);
    repository.deleteById(id);
    afterDelete(id, context);
  }

  default void beforeDelete(ID id, Map<String, Object> context) {}

  default void afterDelete(ID id, Map<String, Object> context) {}

  // ============================ DELETE ALL ============================
  default void defaultDeleteAll(Iterable<ID> ids, CommonRepository<E, ID> repository) {
    Map<String, Object> context = new HashMap<>();
    beforeDeleteAll(ids, context);
    repository.deleteAllByIdInBatch(ids);
    afterDeleteAll(ids, context);
  }

  default void beforeDeleteAll(Iterable<ID> ids, Map<String, Object> context) {}

  default void afterDeleteAll(Iterable<ID> ids, Map<String, Object> context) {}
}
