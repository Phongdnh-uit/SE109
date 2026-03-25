package com.uit.se109.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
  @NotBlank
  @Size(min = 4, max = 50)
  private String username;

  @NotBlank(message = "Password không được để trống")
  @Size(min = 6)
  private String password;

  @Email
  @NotBlank(message = "Email không được để trống")
  private String email;

  private String fullName;

  @Pattern(regexp = "^\\d{10,11}$")
  private String phoneNumber;
}
