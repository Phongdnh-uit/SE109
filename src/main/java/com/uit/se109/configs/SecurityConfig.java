package com.uit.se109.configs;

import com.uit.se109.constants.SecurityConstant;
import com.uit.se109.securities.filter.PrometheusSecurityFilter;
import com.uit.se109.securities.jwt.CustomJwtConverter;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.security.autoconfigure.actuate.web.servlet.EndpointRequest;
import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
  private final AppProperties appProperties;

  public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS256;

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Order(2)
  SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      CustomJwtConverter customJwtConverter,
      PrometheusSecurityFilter prometheusSecurityFilter)
      throws Exception {
    http.addFilterBefore(prometheusSecurityFilter, BearerTokenAuthenticationFilter.class)
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                    .permitAll()
                    .requestMatchers(EndpointRequest.to("health", "info", "prometheus"))
                    .permitAll()
                    .requestMatchers(SecurityConstant.PUBLIC_URLS)
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/v1/properties/me")
                    .authenticated()
                    .requestMatchers(HttpMethod.GET, SecurityConstant.PUBLIC_GET_URLS)
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .oauth2ResourceServer(
            oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(customJwtConverter)));

    return http.build();
  }

  @Bean
  JwtDecoder jwtDecoder() {
    SecretKey originalKey =
        new SecretKeySpec(
            appProperties.getSecurity().getSecretKey().getBytes(), JWT_ALGORITHM.getName());
    return NimbusJwtDecoder.withSecretKey(originalKey).build();
  }
}
