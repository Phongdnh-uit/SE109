package com.uit.se109.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

  @NotBlank
  @Size(min = 4, max = 50)
  private String username;

  @NotBlank
  @Size(min = 8)
  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$")
  private String password;

  @NotBlank @Email private String email;

  @NotBlank private String fullName;

  @Pattern(regexp = "^(0|\\+84)(\\d{9,10})$")
  private String phoneNumber;
}
