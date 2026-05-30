package com.uit.se109.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uit.se109.configs.AppProperties;
import com.uit.se109.configs.CorsConfig;
import com.uit.se109.configs.SecurityConfig;
import com.uit.se109.configs.SwaggerConfig;
import com.uit.se109.dto.PageResponse;
import com.uit.se109.dto.user.UserRequest;
import com.uit.se109.dto.user.UserResponse;
import com.uit.se109.dto.user.UserSummary;
import com.uit.se109.entities.User;
import com.uit.se109.securities.filter.PrometheusSecurityFilter;
import com.uit.se109.securities.jwt.CustomJwtConverter;
import com.uit.se109.services.CrudService;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = UserController.class, // Đổi tên ở đây
    excludeAutoConfiguration = {SecurityAutoConfiguration.class},
    excludeFilters =
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = {
              SecurityConfig.class,
              CorsConfig.class,
              SwaggerConfig.class,
              AppProperties.class,
              PrometheusSecurityFilter.class,
              CustomJwtConverter.class
            }))
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

  @Autowired private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @MockitoBean
  private CrudService<User, Long, UserRequest, UserRequest, UserResponse, UserSummary> userService;

  private UserRequest userRequest;
  private UserResponse userResponse;
  private UserSummary userSummary;

  @BeforeEach
  void setUp() {
    userRequest = new UserRequest();
    userRequest.setUsername("testuser");
    userRequest.setPassword("password123");
    userRequest.setEmail("testuser@test.com");
    userRequest.setFullName("Test User");
    userRequest.setPhoneNumber("01234567890");

    userResponse = new UserResponse();
    userResponse.setId(1L);
    userResponse.setUsername("testuser");
    userResponse.setEmail("testuser@test.com");
    userResponse.setFullName("Test User");
    userResponse.setPhoneNumber("01234567890");

    userSummary = new UserSummary();
    userSummary.setId(1L);
    userSummary.setUsername("testuser");
    userSummary.setEmail("testuser@test.com");
  }

  @Test
  @DisplayName("GET /api/v1/users - Find All Users")
  void testFindAllUsers() throws Exception {
    PageResponse<UserSummary> pageResponse = new PageResponse<>();
    pageResponse.setPage(0);
    pageResponse.setSize(10);
    pageResponse.setTotalElements(1L);
    pageResponse.setTotalPages(1);
    pageResponse.setNumberOfElements(1);
    pageResponse.setContent(Arrays.asList(userSummary));

    when(userService.findAll(any(), any())).thenReturn(pageResponse);

    mockMvc
        .perform(get("/api/v1/users").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(1000))
        .andExpect(jsonPath("$.data.page").value(0))
        .andExpect(jsonPath("$.data.totalElements").value(1))
        .andExpect(jsonPath("$.data.content").isArray())
        .andExpect(jsonPath("$.data.content[0].username").value("testuser"));

    verify(userService, times(1)).findAll(any(), any());
  }

  @Test
  @DisplayName("GET /api/v1/users - Find All Users with Pagination")
  void testFindAllUsersWithPagination() throws Exception {
    PageResponse<UserSummary> pageResponse = new PageResponse<>();
    pageResponse.setPage(0);
    pageResponse.setSize(5);
    pageResponse.setTotalElements(10L);
    pageResponse.setTotalPages(2);
    pageResponse.setNumberOfElements(5);
    pageResponse.setContent(Arrays.asList(userSummary));

    when(userService.findAll(any(), any())).thenReturn(pageResponse);

    mockMvc
        .perform(get("/api/v1/users?page=0&size=5").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.page").value(0))
        .andExpect(jsonPath("$.data.size").value(5))
        .andExpect(jsonPath("$.data.totalPages").value(2));

    verify(userService, times(1)).findAll(any(), any());
  }

  @Test
  @DisplayName("GET /api/v1/users/{id} - Find User By ID")
  void testFindUserById() throws Exception {
    when(userService.findById(1L)).thenReturn(userResponse);

    mockMvc
        .perform(get("/api/v1/users/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(1000))
        .andExpect(jsonPath("$.data.id").value(1))
        .andExpect(jsonPath("$.data.username").value("testuser"))
        .andExpect(jsonPath("$.data.email").value("testuser@test.com"));

    verify(userService, times(1)).findById(1L);
  }

  @Test
  @DisplayName("POST /api/v1/users - Create User")
  void testCreateUser() throws Exception {
    when(userService.create(any(UserRequest.class))).thenReturn(userResponse);

    mockMvc
        .perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(1000))
        .andExpect(jsonPath("$.data.username").value("testuser"))
        .andExpect(jsonPath("$.data.email").value("testuser@test.com"));

    verify(userService, times(1)).create(any(UserRequest.class));
  }

  @Test
  @DisplayName("POST /api/v1/users - Create User with Invalid Email")
  void testCreateUserInvalidEmail() throws Exception {
    UserRequest invalidRequest = new UserRequest();
    invalidRequest.setUsername("testuser");
    invalidRequest.setPassword("password123");
    invalidRequest.setEmail("invalid-email");

    mockMvc
        .perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
        .andExpect(status().isBadRequest());

    verify(userService, never()).create(any(UserRequest.class));
  }

  @Test
  @DisplayName("POST /api/v1/users - Create User with Invalid Password")
  void testCreateUserInvalidPassword() throws Exception {
    UserRequest invalidRequest = new UserRequest();
    invalidRequest.setUsername("testuser");
    invalidRequest.setPassword("short");
    invalidRequest.setEmail("test@test.com");

    mockMvc
        .perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
        .andExpect(status().isBadRequest());

    verify(userService, never()).create(any(UserRequest.class));
  }

  @Test
  @DisplayName("PUT /api/v1/users/{id} - Update User")
  void testUpdateUser() throws Exception {
    when(userService.update(anyLong(), any(UserRequest.class))).thenReturn(userResponse);

    mockMvc
        .perform(
            put("/api/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(1000))
        .andExpect(jsonPath("$.data.username").value("testuser"));

    verify(userService, times(1)).update(anyLong(), any(UserRequest.class));
  }

  @Test
  @DisplayName("DELETE /api/v1/users/{id} - Delete User")
  void testDeleteUser() throws Exception {
    doNothing().when(userService).delete(1L);

    mockMvc
        .perform(delete("/api/v1/users/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(1000))
        .andExpect(jsonPath("$.message").value("success"));

    verify(userService, times(1)).delete(1L);
  }

  @Test
  @DisplayName("DELETE /api/v1/users - Delete Multiple Users")
  void testDeleteMultipleUsers() throws Exception {
    doNothing().when(userService).deleteAll(Arrays.asList(1L, 2L, 3L));

    mockMvc
        .perform(delete("/api/v1/users?ids=1&ids=2&ids=3").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(1000));

    verify(userService, times(1)).deleteAll(Arrays.asList(1L, 2L, 3L));
  }
}
