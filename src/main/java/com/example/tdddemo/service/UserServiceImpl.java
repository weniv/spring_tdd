package com.example.tdddemo.service;

import com.example.tdddemo.entity.User;
import com.example.tdddemo.repository.UserRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public User registerUser(String email, String password, String name) {
    // 1. 이메일 중복 체크
    if (isEmailTaken(email)) {
      throw new IllegalArgumentException("Email is already taken");
    }
    // 2. 비밀번호 암호화
    String encodedPassword = passwordEncoder.encode(password);
    User newUser = new User(email, encodedPassword, name);
    return userRepository.save(newUser);
  }

  @Override
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Override
  public User getUserByEmail(String email) throws NoSuchElementException {
    User user = userRepository.findByEmail(email);
    if (user == null) {
      throw new NoSuchElementException("User not found");
    }
    return user;
  }

  @Override
  public User updateUser(String email, String name) {
    User user = userRepository.findByEmail(email);
    if (user != null) {
      user.setName(name);
      return userRepository.save(user);
    }
    return null;
  }

  @Override
  public void deleteUser(String email) {
    User user = userRepository.findByEmail(email);
    if (user != null) {
      userRepository.delete(user);
    }
  }

  @Override
  public boolean isEmailTaken(String email) {
    return userRepository.existsByEmail(email);
  }

  @Override
  public User updateUser(String email, String currentPassword, String newName) {
    User user = getUserByEmail(email);
    if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
      throw new IllegalArgumentException("Invalid email or password");
    }
    user.setName(newName);
    return userRepository.save(user);
  }

  @Override
  public void deleteUser(String email, String password)
      throws NoSuchElementException, IllegalArgumentException {
    User user = userRepository.findByEmail(email);
    if (user == null) {
      throw new NoSuchElementException("User not found");
    }
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new IllegalArgumentException("Invalid email or password");
    }
    userRepository.delete(user);
  }
}