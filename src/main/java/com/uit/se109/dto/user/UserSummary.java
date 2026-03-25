package com.uit.se109.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSummary {
  private Long id;
  private String username;
  private String fullName;
  private String email;
}
