package com.uit.se109.services.user;

import com.uit.se109.dto.PageResponse;
import com.uit.se109.dto.user.UserRequest;
import com.uit.se109.dto.user.UserResponse;
import com.uit.se109.dto.user.UserSummary;
import com.uit.se109.entities.User;
import com.uit.se109.mappers.UserMapper;
import com.uit.se109.repositories.UserRepository;
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
  public UserResponse update(Long id, UserRequest input) {
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
