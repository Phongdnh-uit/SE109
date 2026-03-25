package com.uit.se109.helpers;

import com.uit.se109.repositories.CommonRepository;

public class ValidationHelper {
  private ValidationHelper() {}

  public static <ID, E> boolean exists(
      String field, String value, CommonRepository<E, ID> repository) {
    return repository.exists((root, query, builder) -> builder.equal(root.get(field), value));
  }

  public static <ID, E> boolean existsByDifferentId(
      ID id, String field, String value, CommonRepository<E, ID> repository) {
    return repository.exists(
        (root, query, builder) ->
            builder.and(
                builder.notEqual(root.get("id"), id), builder.equal(root.get(field), value)));
  }
}
