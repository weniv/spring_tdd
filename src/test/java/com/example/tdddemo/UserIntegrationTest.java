package com.example.tdddemo;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.tdddemo.controller.UserDeleteDto;
import com.example.tdddemo.controller.UserRegistrationDto;
import com.example.tdddemo.controller.UserUpdateDto;
import com.example.tdddemo.entity.User;
import com.example.tdddemo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class UserIntegrationTest {
  private static final Logger logger = LoggerFactory.getLogger(UserIntegrationTest.class);
  @Autowired private MockMvc mockMvc;

  @Autowired private UserRepository userRepository;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
  }

  @Test
  void testUserFullLifecycle() throws Exception {
    // 사용자 등록
    String email = "test@example.com";
    String password = "password123";
    String name = "Test User";
    UserRegistrationDto registrationDto = new UserRegistrationDto(email, password, name);

    String resultString =
        mockMvc
            .perform(
                post("/api/users/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf())
                    .content(objectMapper.writeValueAsString(registrationDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.email").value(email))
            .andExpect(jsonPath("$.name").value(name))
            .andReturn()
            .getResponse()
            .getContentAsString();

    User savedUser = objectMapper.readValue(resultString, User.class);

    // 사용자 정보 수정
    String newName = "Updated User";
    UserUpdateDto updateDto = new UserUpdateDto(password, newName);

    mockMvc
        .perform(
            put("/api/users/" + email)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(updateDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(newName));

    // 수정된 사용자 정보 조회
    mockMvc
        .perform(get("/api/users/" + email).with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value(email))
        .andExpect(jsonPath("$.name").value(newName));

    // 사용자 삭제
    UserDeleteDto deleteDto = new UserDeleteDto(password);
    logger.info("Attempting to delete user: {}", email);
    MvcResult result =
        mockMvc
            .perform(
                delete("/api/users/" + email)
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf())
                    .content(objectMapper.writeValueAsString(deleteDto)))
            .andExpect(status().isNoContent())
            .andReturn();
    logger.info("Delete response status: {}", result.getResponse().getStatus());
    logger.info("Delete response content: {}", result.getResponse().getContentAsString());
    // 삭제된 사용자 조회 시도
    mockMvc.perform(get("/api/users/" + email).with(csrf())).andExpect(status().isNotFound());
  }

  @Test
  void testDuplicateEmailRegistration() throws Exception {
    // 첫 번째 사용자 등록
    String email = "duplicate@example.com";
    UserRegistrationDto registrationDto =
        new UserRegistrationDto(email, "password123", "First User");

    mockMvc
        .perform(
            post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(registrationDto)))
        .andExpect(status().isCreated());

    // 동일한 이메일로 두 번째 사용자 등록 시도
    UserRegistrationDto duplicateDto =
        new UserRegistrationDto(email, "anotherpassword", "Second User");

    mockMvc
        .perform(
            post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(duplicateDto)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Email is already taken"));
  }

  @Test
  void testNonExistentUserOperations() throws Exception {
    String nonExistentEmail = "nonexistent@example.com";

    // 존재하지 않는 사용자 조회 시도
    mockMvc
        .perform(get("/api/users/" + nonExistentEmail).with(csrf()))
        .andExpect(status().isNotFound())
        .andExpect(content().string("User not found"));

    // 존재하지 않는 사용자 삭제 시도
    mockMvc
        .perform(
            delete("/api/users/" + nonExistentEmail)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(new UserDeleteDto("somepassword"))))
        .andExpect(status().isNotFound())
        .andExpect(content().string("User not found"));
  }

  @Test
  void testInvalidPasswordOnUpdate() throws Exception {
    // 사용자 등록
    String email = "test@example.com";
    String password = "password123";
    String name = "Test User";
    UserRegistrationDto registrationDto = new UserRegistrationDto(email, password, name);

    mockMvc
        .perform(
            post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(registrationDto)))
        .andExpect(status().isCreated());

    // 잘못된 비밀번호로 사용자 정보 수정 시도
    String newName = "Updated User";
    UserUpdateDto updateDto = new UserUpdateDto("wrongpassword", newName);

    mockMvc
        .perform(
            put("/api/users/" + email)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(updateDto)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Invalid email or password")));
  }

  @Test
  void testInvalidPasswordOnDelete() throws Exception {
    // 사용자 등록
    String email = "test@example.com";
    String password = "correctpassword";
    String name = "Test User";
    UserRegistrationDto registrationDto = new UserRegistrationDto(email, password, name);

    mockMvc
        .perform(
            post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(registrationDto)))
        .andExpect(status().isCreated());

    // 잘못된 비밀번호로 사용자 삭제 시도
    mockMvc
        .perform(
            delete("/api/users/" + email)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(new UserDeleteDto("wrongpassword"))))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Invalid email or password"));
  }
}