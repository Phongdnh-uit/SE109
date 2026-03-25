package com.uit.se109.securities.jwt;

import com.uit.se109.entities.User;
import com.uit.se109.repositories.UserRepository;
import com.uit.se109.securities.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  private final UserRepository userRepository;

  // private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;

  @Override
  public AbstractAuthenticationToken convert(Jwt jwt) {
    // Collection<? extends GrantedAuthority> jwtAuthorities =
    //     jwtGrantedAuthoritiesConverter.convert(jwt);
    // Set<? extends GrantedAuthority> authorities =
    //     jwtAuthorities == null
    //         ? Collections.emptySet()
    //         : Collections.unmodifiableSet(Set.copyOf(jwtAuthorities));

    Long userId = null;
    try {
      userId = Long.parseLong(jwt.getSubject());
    } catch (NumberFormatException e) {
      throw new JwtException("Invalid JWT subject: " + jwt.getSubject());
    }
    User user =
        userRepository.findById(userId).orElseThrow(() -> new JwtException("User not found."));

    CustomUserDetails principal = CustomUserDetails.fromUserEntity(user);
    return new UsernamePasswordAuthenticationToken(principal, jwt, principal.getAuthorities());
  }
}
