package com.example.tdddemo.service;

import com.example.tdddemo.entity.User;
import java.util.List;
import java.util.NoSuchElementException;

public interface UserService {
  User registerUser(String email, String password, String name);

  User getUserByEmail(String email) throws NoSuchElementException;

  void deleteUser(String email, String password)
      throws NoSuchElementException, IllegalArgumentException;

  List<User> getAllUsers();

  User updateUser(String email, String name);

  void deleteUser(String email);

  boolean isEmailTaken(String email);

  User updateUser(String email, String currentPassword, String newName);
}