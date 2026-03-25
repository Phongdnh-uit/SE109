package com.uit.se109.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(@NotBlank String oldPassword, @NotBlank String newPassword) {}
