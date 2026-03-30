package com.uit.se109.constants;

public interface SecurityConstant {
  String[] PUBLIC_URLS = {
    "/swagger-ui/**",
    "/v3/api-docs/**",
    "/api/v1/auth/register",
    "/api/v1/auth/login",
    "/api/v1/auth/refresh"
  };
}
