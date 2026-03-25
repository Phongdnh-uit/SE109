package com.uit.se109.repositories;

import com.uit.se109.entities.RefreshToken;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CommonRepository<RefreshToken, Long> {}
