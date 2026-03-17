package com.uit.se109.controllers;

import com.uit.se109.dto.ApiResponse;
import com.uit.se109.dto.PageResponse;
import com.uit.se109.services.CrudService;
import io.github.perplexhub.rsql.RSQLJPASupport;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
public abstract class GenericController<E, ID, C, U, O, S> {

  protected final CrudService<E, ID, C, U, O, S> service;

  @Operation(operationId = "findAll{Resource}")
  @GetMapping
  public ResponseEntity<ApiResponse<PageResponse<S>>> findAll(
      @ParameterObject Pageable pageable,
      @RequestParam(value = "filter", required = false) @Nullable String filter,
      @RequestParam(value = "all", defaultValue = "false") boolean all) {
    Specification<E> specification = RSQLJPASupport.toSpecification(filter);
    if (all) {
      pageable = Pageable.unpaged(pageable.getSort());
    }
    return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable, specification)));
  }

  @Operation(operationId = "find{Resource}ById")
  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<O>> findById(@PathVariable("id") ID id) {
    return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
  }

  @Operation(operationId = "create{Resource}")
  @PostMapping
  public ResponseEntity<ApiResponse<O>> create(@Valid @RequestBody C input) {
    return ResponseEntity.ok(ApiResponse.ok(service.create(input)));
  }

  @Operation(operationId = "update{Resource}")
  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<O>> update(
      @PathVariable("id") ID id, @Valid @RequestBody U input) {
    return ResponseEntity.ok(ApiResponse.ok(service.update(id, input)));
  }

  @Operation(operationId = "delete{Resource}ById")
  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> delete(@PathVariable("id") ID id) {
    service.delete(id);
    return ResponseEntity.ok(ApiResponse.ok(null));
  }

  @Operation(operationId = "deleteBulk{Resource}")
  @DeleteMapping
  public ResponseEntity<ApiResponse<Void>> deleteAll(@RequestParam("ids") List<ID> ids) {
    service.deleteAll(ids);
    return ResponseEntity.ok(ApiResponse.ok(null));
  }
}
