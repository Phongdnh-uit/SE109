package com.uit.se109.services.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.uit.se109.dto.user.UserRequest;
import com.uit.se109.dto.user.UserResponse;
import com.uit.se109.entities.User;
import com.uit.se109.exception.AppException;
import com.uit.se109.exception.ErrorCode;
import com.uit.se109.mappers.UserMapper;
import com.uit.se109.repositories.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock private UserRepository userRepository;
  @Mock private UserMapper userMapper;

  @InjectMocks private UserServiceImpl userService;

  private UserRequest userRequest;
  private User user;
  private UserResponse userResponse;

  @BeforeEach
  void setUp() {
    userRequest = new UserRequest();
    userRequest.setUsername("testuser");
    userRequest.setEmail("test@example.com");
    userRequest.setPhoneNumber("0123456789");

    user = new User();
    user.setId(1L);
    user.setUsername("testuser");
    user.setEmail("test@example.com");
    user.setPhoneNumber("0123456789");

    userResponse = new UserResponse();
    userResponse.setId(1L);
    userResponse.setUsername("testuser");
    userResponse.setEmail("test@example.com");
    userResponse.setPhoneNumber("0123456789");
  }

  @Test
  void shouldCreateUserSuccessfully() {
    // Arrange
    when(userRepository.exists(any(Specification.class))).thenReturn(false);
    when(userMapper.requestToEntity(any(UserRequest.class))).thenReturn(user);
    when(userRepository.save(any(User.class))).thenReturn(user);
    when(userMapper.entityToResponse(any(User.class))).thenReturn(userResponse);

    // Act
    UserResponse result = userService.create(userRequest);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getUsername()).isEqualTo("testuser");
    verify(userRepository, times(1)).save(user);
    // exists is called 3 times (email, phone, username)
    verify(userRepository, times(3)).exists(any(Specification.class));
  }

  @Test
  void shouldThrowExceptionWhenCreateUserWithExistingEmail() {
    // Arrange
    // Return true for the first exists call (email)
    when(userRepository.exists(any(Specification.class)))
        .thenReturn(true)
        .thenReturn(false)
        .thenReturn(false);

    // Act & Assert
    assertThatThrownBy(() -> userService.create(userRequest))
        .isInstanceOf(AppException.class)
        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.VALIDATION_ERROR);
  }

  @Test
  void shouldUpdateUserSuccessfully() {
    // Arrange
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userRepository.exists(any(Specification.class))).thenReturn(false);
    doNothing().when(userMapper).partialUpdate(userRequest, user);
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.entityToResponse(user)).thenReturn(userResponse);

    // Act
    UserResponse result = userService.update(1L, userRequest);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(1L);
    verify(userRepository, times(1)).save(user);
    verify(userRepository, times(3)).exists(any(Specification.class));
  }

  @Test
  void shouldThrowExceptionWhenUpdateUserWithExistingUsername() {
    // Arrange
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    // Return true for exists (meaning it exists for a different user ID)
    when(userRepository.exists(any(Specification.class)))
        .thenReturn(false)
        .thenReturn(false)
        .thenReturn(true);

    // Act & Assert
    assertThatThrownBy(() -> userService.update(1L, userRequest))
        .isInstanceOf(AppException.class)
        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.VALIDATION_ERROR);
  }

  @Test
  void shouldFindByIdSuccessfully() {
    // Arrange
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userMapper.entityToResponse(user)).thenReturn(userResponse);

    // Act
    UserResponse result = userService.findById(1L);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(1L);
    verify(userRepository, times(1)).findById(1L);
  }

  @Test
  void shouldThrowExceptionWhenUserNotFoundById() {
    // Arrange
    when(userRepository.findById(99L)).thenReturn(Optional.empty());

    // Act & Assert
    assertThatThrownBy(() -> userService.findById(99L))
        .isInstanceOf(AppException.class)
        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.RESOURCE_NOT_FOUND);
  }

  @Test
  void shouldDeleteUserSuccessfully() {
    // Arrange
    doNothing().when(userRepository).deleteById(1L);

    // Act
    userService.delete(1L);

    // Assert
    verify(userRepository, times(1)).deleteById(1L);
  }
}
