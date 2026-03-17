package com.uit.se109.mappers;

import org.mapstruct.MappingTarget;

public interface GenericMapper<E, C, U, O, S> {
  E requestToEntity(C request);

  O entityToResponse(E entity);

  S entityToSummaryResponse(E entity);

  void partialUpdate(U request, @MappingTarget E entity);
}
