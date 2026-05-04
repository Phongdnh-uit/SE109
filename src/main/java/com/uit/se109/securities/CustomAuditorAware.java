package com.uit.se109.securities;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
public class CustomAuditorAware implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.ofNullable(
        SecurityUtil.getCurrentUserId() != null
            ? SecurityUtil.getCurrentUserId().toString()
            : null);
  }
}
