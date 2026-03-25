package com.uit.se109.controllers;

import com.uit.se109.dto.user.UserRequest;
import com.uit.se109.dto.user.UserResponse;
import com.uit.se109.dto.user.UserSummary;
import com.uit.se109.entities.User;
import com.uit.se109.services.CrudService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "Endpoints for managing users")
@RequestMapping("/api/v1/users")
@RestController
public class UserController
    extends GenericController<User, Long, UserRequest, UserRequest, UserResponse, UserSummary> {

  public UserController(
      CrudService<User, Long, UserRequest, UserRequest, UserResponse, UserSummary> service) {
    super(service);
  }
}
