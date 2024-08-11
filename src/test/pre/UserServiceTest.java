package com.example.tdddemo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private UserService userService;

  @Test
  void getUserById_존재하는_사용자_조회_성공() {
    // Given
    Long userId = 1L;
    User expectedUser = new User(userId, "testuser");
    when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

    // When
    User actualUser = userService.getUserById(userId);

    // Then
    assertEquals(expectedUser, actualUser);
    verify(userRepository).findById(userId);
  }

  @Test
  void getUserById_존재하지_않는_사용자_조회_실패() {
    // Given
    Long userId = 1L;
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
    verify(userRepository).findById(userId);
  }
}