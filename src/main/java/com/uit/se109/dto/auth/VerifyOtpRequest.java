package com.uit.se109.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VerifyOtpRequest(
    @NotBlank @Email String email, @NotBlank @Size(min = 6, max = 6) String otp) {}
