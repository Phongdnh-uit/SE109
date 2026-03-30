package com.uit.se109.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application")
public class AppProperties {
  private Frontend frontend;
  private Security security;

  @Getter
  @Setter
  public static class Frontend {
    private String baseUrl;
  }

  @Getter
  @Setter
  public static class Security {
    private String secretKey;
    private long accessTokenExpirationInMillis;
    private long refreshTokenExpirationInMillis;
    private String monitoringSecret;
  }
}
