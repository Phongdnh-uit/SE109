package com.uit.se109.services.user;

import com.uit.se109.dto.PageResponse;
import com.uit.se109.dto.user.UserRequest;
import com.uit.se109.dto.user.UserResponse;
import com.uit.se109.dto.user.UserSummary;
import com.uit.se109.entities.User;
import com.uit.se109.exception.AppException;
import com.uit.se109.exception.ErrorCode;
import com.uit.se109.helpers.ValidationHelper;
import com.uit.se109.mappers.UserMapper;
import com.uit.se109.repositories.UserRepository;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository repository;
  private final UserMapper mapper;

  @Override
  public PageResponse<UserSummary> findAll(Pageable pageable, Specification<User> specification) {
    return defaultFindAll(pageable, specification, mapper, repository);
  }

  @Override
  public UserResponse findById(Long id) {
    return defaultFindById(id, mapper, repository);
  }

  @Override
  public UserResponse create(UserRequest input) {
    return defaultCreate(input, mapper, repository);
  }

  @Override
  public void beforeCreate(UserRequest input, Map<String, Object> context) {
    Map<String, String> errors = new HashMap<>();
    // Check (phone, email, username)
    if (ValidationHelper.exists("email", input.getEmail(), repository)) {
      errors.put("email", "Email already exists");
    }
    if (ValidationHelper.exists("phoneNumber", input.getPhoneNumber(), repository)) {
      errors.put("phoneNumber", "Phone already exists");
    }
    if (ValidationHelper.exists("username", input.getUsername(), repository)) {
      errors.put("username", "Username already exists");
    }
    if (!errors.isEmpty()) {
      throw new AppException(ErrorCode.VALIDATION_ERROR, errors);
    }
  }

  @Override
  public UserResponse update(Long id, UserRequest input) {
    return defaultUpdate(id, input, mapper, repository);
  }

  @Override
  public void beforeUpdate(Long id, UserRequest input, User entity, Map<String, Object> context) {
    Map<String, String> errors = new HashMap<>();
    // Check (phone, email, username)
    if (ValidationHelper.existsByDifferentId(id, "email", input.getEmail(), repository)) {
      errors.put("email", "Email already exists");
    }
    if (ValidationHelper.existsByDifferentId(id, "phoneNumber", input.getPhoneNumber(), repository)) {
      errors.put("phoneNumber", "Phone already exists");
    }
    if (ValidationHelper.existsByDifferentId(id, "username", input.getUsername(), repository)) {
      errors.put("username", "Username already exists");
    }
    if (!errors.isEmpty()) {
      throw new AppException(ErrorCode.VALIDATION_ERROR, errors);
    }
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
