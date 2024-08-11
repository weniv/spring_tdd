package com.example.tdddemo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.tdddemo.entity.User;
import com.example.tdddemo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = UserController.class)
@WithMockUser(username = "test")
public class UserControllerTest {
  @Autowired private MockMvc mockMvc;

  @MockBean private UserService userService;

  @Autowired private ObjectMapper objectMapper;

  private User testUser;

  @BeforeEach
  void setUp() {
    testUser = new User("test@example.com", "password", "Test User");
  }

  @Test
  void testRegisterUser() throws Exception {
    UserRegistrationDto registrationDto =
        new UserRegistrationDto("test@example.com", "password", "Test User");
    when(userService.registerUser(any(), any(), any())).thenReturn(testUser);

    mockMvc
        .perform(
            post("/api/users/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.email").value("test@example.com"))
        .andExpect(jsonPath("$.name").value("Test User"));
  }

  @Test
  void testGetUser() throws Exception {
    when(userService.getUserByEmail("test@example.com")).thenReturn(testUser);

    mockMvc
        .perform(get("/api/users/test@example.com").with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("test@example.com"))
        .andExpect(jsonPath("$.name").value("Test User"));
  }

  @Test
  void testUpdateUser() throws Exception {
    UserUpdateDto updateDto = new UserUpdateDto("newpassword", "Updated User");
    User updatedUser = new User("test@example.com", "newpassword", "Updated User");
    when(userService.updateUser(any(), any(), any())).thenReturn(updatedUser);

    mockMvc
        .perform(
            put("/api/users/test@example.com")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Updated User"));
  }

  @Test
  void testDeleteUser() throws Exception {
    UserDeleteDto deleteDto = new UserDeleteDto("password");

    mockMvc
        .perform(
            delete("/api/users/test@example.com")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deleteDto)))
        .andExpect(status().isNoContent());
  }

  @Test
  void testGetAllUsers() throws Exception {
    List<User> users =
        Arrays.asList(
            new User("user1@example.com", "password", "User 1"),
            new User("user2@example.com", "password", "User 2"));
    when(userService.getAllUsers()).thenReturn(users);

    mockMvc
        .perform(get("/api/users").with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].email").value("user1@example.com"))
        .andExpect(jsonPath("$[1].email").value("user2@example.com"));
  }

  // 2. 이메일 형식 검증 테스트
  @Test
  void testRegisterUserWithInvalidEmail() throws Exception {
    UserRegistrationDto invalidDto =
        new UserRegistrationDto("invalid-email", "password", "Test User");

    mockMvc
        .perform(
            post("/api/users/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
        .andExpect(status().isBadRequest());
  }

  // 3. 존재하지 않는 사용자 조회 테스트
  @Test
  void testGetNonExistentUser() throws Exception {
    when(userService.getUserByEmail("nonexistent@example.com")).thenReturn(null);

    mockMvc
        .perform(get("/api/users/nonexistent@example.com").with(csrf()))
        .andExpect(status().isNotFound());
  }
}