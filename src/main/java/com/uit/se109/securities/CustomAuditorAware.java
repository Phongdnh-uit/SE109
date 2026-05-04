package com.uit.se109.securities;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;

public class CustomAuditorAware implements AuditorAware<Long> {

  @Override
  public Optional<Long> getCurrentAuditor() {
    return Optional.ofNullable(SecurityUtil.getCurrentUserId());
  }
}
