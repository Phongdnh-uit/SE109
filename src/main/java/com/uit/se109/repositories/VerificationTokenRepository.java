package com.uit.se109.repositories;

import com.uit.se109.entities.User;
import com.uit.se109.entities.VerificationToken;
import com.uit.se109.enums.VerificationTokenType;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends CommonRepository<VerificationToken, Long> {
  Optional<VerificationToken> findByTokenAndUserAndType(
      String token, User user, VerificationTokenType type);

  Optional<VerificationToken> findByTokenAndType(String token, VerificationTokenType type);
}
