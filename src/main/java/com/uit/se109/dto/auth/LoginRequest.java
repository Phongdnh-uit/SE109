package com.uit.se109.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank String credential, @NotBlank String password) {}
