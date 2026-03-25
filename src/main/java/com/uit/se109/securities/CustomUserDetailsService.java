package com.uit.se109.securities;

import com.uit.se109.entities.User;
import com.uit.se109.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // Email only for now, can be extended to support username or phone number
    User user =
        userRepository
            .findOne((root, query, builder) -> builder.equal(root.get("email"), username))
            .orElseThrow(
                () -> new UsernameNotFoundException("User not found with email: " + username));
    return CustomUserDetails.fromUserEntity(user);
  }
}
