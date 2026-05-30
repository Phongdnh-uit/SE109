package com.uit.se109.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.uit.se109.entities.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.jpa.domain.Specification;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryTest {

  @Container @ServiceConnection static MySQLContainer mysql = new MySQLContainer("mysql:latest");

  @Autowired private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();

    User u1 = new User();
    u1.setUsername("user1");
    u1.setEmail("user1@example.com");
    u1.setPassword("pass");

    User u2 = new User();
    u2.setUsername("user2");
    u2.setEmail("user2@example.com");
    u2.setPassword("pass");

    userRepository.saveAll(List.of(u1, u2));
  }

  @Test
  void shouldExistsByUsernameSpecification() {
    Specification<User> spec = (root, query, cb) -> cb.equal(root.get("username"), "user1");

    boolean exists = userRepository.exists(spec);

    assertThat(exists).isTrue();
  }

  @Test
  void shouldNotExistsByNonExistentUsername() {
    Specification<User> spec = (root, query, cb) -> cb.equal(root.get("username"), "nonexistent");

    boolean exists = userRepository.exists(spec);

    assertThat(exists).isFalse();
  }

  @Test
  void shouldExistsByDifferentIdAndEmail() {
    List<User> users = userRepository.findAll();
    User u1 = users.stream().filter(u -> u.getUsername().equals("user1")).findFirst().get();
    User u2 = users.stream().filter(u -> u.getUsername().equals("user2")).findFirst().get();

    // Check if email 'user2@example.com' exists for a user ID different from u1's ID.
    // This simulates the validation when u1 tries to update their email to u2's email.
    Specification<User> spec =
        (root, query, cb) ->
            cb.and(
                cb.notEqual(root.get("id"), u1.getId()),
                cb.equal(root.get("email"), "user2@example.com"));

    boolean exists = userRepository.exists(spec);

    assertThat(exists).isTrue();
  }
}
