package com.uit.se109.securities;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.uit.se109.configs.AppProperties;
import com.uit.se109.exception.AppException;
import com.uit.se109.exception.ErrorCode;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtProvider {
  private final AppProperties appProperties;

  public String generateToken(Long userId) {
    try {
      JWSSigner signer = new MACSigner(appProperties.getSecurity().getSecretKey());

      JWTClaimsSet claimsSet =
          new JWTClaimsSet.Builder()
              .subject(userId.toString())
              .issuer("se109")
              .issueTime(new Date())
              .expirationTime(
                  new Date(
                      System.currentTimeMillis()
                          + appProperties.getSecurity().getAccessTokenExpirationInMillis()))
              .jwtID(UUID.randomUUID().toString())
              .build();

      SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

      signedJWT.sign(signer);

      return signedJWT.serialize();
    } catch (JOSEException e) {
      log.error("Error generating token", e);
      throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
    }
  }
}
