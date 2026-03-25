package com.uit.se109.services.user;

import com.uit.se109.dto.user.UserRequest;
import com.uit.se109.dto.user.UserResponse;
import com.uit.se109.dto.user.UserSummary;
import com.uit.se109.entities.User;
import com.uit.se109.services.CrudService;

public interface UserService
    extends CrudService<User, Long, UserRequest, UserRequest, UserResponse, UserSummary> {}
