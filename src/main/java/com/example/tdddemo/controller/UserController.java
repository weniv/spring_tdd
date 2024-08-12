package com.example.tdddemo.controller;

import com.example.tdddemo.entity.User;
import com.example.tdddemo.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto registrationDto) {
    try {
      User user =
          userService.registerUser(
              registrationDto.getEmail(), registrationDto.getPassword(), registrationDto.getName());
      return new ResponseEntity<>(user, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/{email}")
  public ResponseEntity<?> getUser(@PathVariable String email) {
    try {
      User user = userService.getUserByEmail(email);
      return new ResponseEntity<>(user, HttpStatus.OK);
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/{email}")
  public ResponseEntity<?> updateUser(
      @PathVariable String email, @RequestBody UserUpdateDto updateDto) {
    try {
      User updatedUser =
          userService.updateUser(email, updateDto.getCurrentPassword(), updateDto.getNewName());
      return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping("/{email}")
  public ResponseEntity<?> deleteUser(
      @PathVariable String email, @RequestBody UserDeleteDto deleteDto) {
    try {
      userService.deleteUser(email, deleteDto.getPassword());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}