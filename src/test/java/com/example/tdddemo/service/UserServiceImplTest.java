package com.example.tdddemo.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.tdddemo.entity.User;
import com.example.tdddemo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceImplTest {
  @Mock private UserRepository userRepository;
  @Mock private PasswordEncoder passwordEncoder;
  @InjectMocks private UserServiceImpl userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testRegisterUser() {
    User newUser = new User("test@example.com", "password", "Test User");
    when(userRepository.save(any(User.class))).thenReturn(newUser);

    User registeredUser = userService.registerUser("test@example.com", "password", "Test User");

    assertThat(registeredUser).isNotNull();
    assertThat(registeredUser.getEmail()).isEqualTo("test@example.com");
    assertThat(registeredUser.getName()).isEqualTo("Test User");
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  void testGetUserByEmail() {
    User user = new User("test@example.com", "password", "Test User");
    when(userRepository.findByEmail("test@example.com")).thenReturn(user);

    User foundUser = userService.getUserByEmail("test@example.com");

    assertThat(foundUser).isNotNull();
    assertThat(foundUser.getEmail()).isEqualTo("test@example.com");
    verify(userRepository, times(1)).findByEmail("test@example.com");
  }

  @Test
  void testUpdateUser() {
    User existingUser = new User("test@example.com", "password", "Test User");
    User updatedUser = new User("test@example.com", "password", "Updated User");
    when(userRepository.findByEmail("test@example.com")).thenReturn(existingUser);
    when(userRepository.save(any(User.class))).thenReturn(updatedUser);

    User result = userService.updateUser("test@example.com", "Updated User");

    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo("Updated User");
    verify(userRepository, times(1)).findByEmail("test@example.com");
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  void testDeleteUser() {
    User user = new User("test@example.com", "password", "Test User");
    when(userRepository.findByEmail("test@example.com")).thenReturn(user);

    userService.deleteUser("test@example.com");

    verify(userRepository, times(1)).findByEmail("test@example.com");
    verify(userRepository, times(1)).delete(user);
  }

  @Test
  void testIsEmailTaken() {
    when(userRepository.existsByEmail("taken@example.com")).thenReturn(true);
    when(userRepository.existsByEmail("available@example.com")).thenReturn(false);

    boolean isTaken = userService.isEmailTaken("taken@example.com");
    boolean isAvailable = userService.isEmailTaken("available@example.com");

    assertThat(isTaken).isTrue();
    assertThat(isAvailable).isFalse();
    verify(userRepository, times(1)).existsByEmail("taken@example.com");
    verify(userRepository, times(1)).existsByEmail("available@example.com");
  }

  @Test
  void testRegisterUser_EmailAlreadyTaken() {
    // 1. 이미 존재하는 이메일로 가정
    when(userRepository.existsByEmail("taken@example.com")).thenReturn(true);

    // 2. 이미 존재하는 이메일로 회원가입 시도
    assertThatThrownBy(() -> userService.registerUser("taken@example.com", "password", "Test User"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Email is already taken");

    // 3. save 메서드가 호출되지 않았는지 확인
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  void testUpdateUser_ValidCredentials() {
    // 1. 기존 사용자 정보 설정
    User existingUser = new User("test@example.com", "encodedPassword", "Test User");
    when(userRepository.findByEmail("test@example.com")).thenReturn(existingUser);
    when(passwordEncoder.matches("currentPassword", "encodedPassword")).thenReturn(true);
    when(userRepository.save(any(User.class))).thenReturn(existingUser);

    // 2. 사용자 정보 업데이트
    User updatedUser =
        userService.updateUser("test@example.com", "currentPassword", "Updated User");

    // 3. 업데이트된 정보 확인
    assertThat(updatedUser.getName()).isEqualTo("Updated User");
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  void testUpdateUser_InvalidCredentials() {
    // 1. 기존 사용자 정보 설정
    User existingUser = new User("test@example.com", "encodedPassword", "Test User");
    when(userRepository.findByEmail("test@example.com")).thenReturn(existingUser);
    when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

    // 2. 잘못된 비밀번호로 업데이트 시도
    assertThatThrownBy(
            () -> userService.updateUser("test@example.com", "wrongPassword", "Updated User"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid email or password");

    // 3. save 메서드가 호출되지 않았는지 확인
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  void testDeleteUser_ValidCredentials() {
    // 1. 기존 사용자 정보 설정
    User existingUser = new User("test@example.com", "encodedPassword", "Test User");
    when(userRepository.findByEmail("test@example.com")).thenReturn(existingUser);
    when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

    // 2. 사용자 삭제
    userService.deleteUser("test@example.com", "password");

    // 3. delete 메서드가 호출되었는지 확인
    verify(userRepository, times(1)).delete(existingUser);
  }

  @Test
  void testDeleteUser_InvalidCredentials() {
    // 1. 기존 사용자 정보 설정
    User existingUser = new User("test@example.com", "encodedPassword", "Test User");
    when(userRepository.findByEmail("test@example.com")).thenReturn(existingUser);
    when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

    // 2. 잘못된 비밀번호로 삭제 시도
    assertThatThrownBy(() -> userService.deleteUser("test@example.com", "wrongPassword"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid email or password");

    // 3. delete 메서드가 호출되지 않았는지 확인
    verify(userRepository, never()).delete(any(User.class));
  }
}