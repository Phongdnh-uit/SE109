package com.uit.se109.securities;

import com.uit.se109.entities.User;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Builder
@Getter
public class CustomUserDetails implements UserDetails {

  private Long id;
  private String password;
  private String fullName;
  private String email;
  private String phoneNumber;
  private String username;
  private Set<? extends GrantedAuthority> authorities;

  public static CustomUserDetails fromUserEntity(User user) {
    return CustomUserDetails.builder()
        .id(user.getId())
        .username(user.getUsername())
        .password(user.getPassword())
        .fullName(user.getFullName())
        .email(user.getEmail())
        .phoneNumber(user.getPhoneNumber())
        .authorities(Set.of())
        .build();
  }
}
