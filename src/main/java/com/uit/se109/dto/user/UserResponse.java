package com.uit.se109.dto.user;

import com.uit.se109.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse extends BaseEntity {
  private String username;
  private String email;
  private String fullName;
  private String phoneNumber;
  private String createdBy;
}
