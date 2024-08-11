package com.example.tdddemo.controller;

import com.example.tdddemo.entity.User;
import com.example.tdddemo.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
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
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 1. 모든 사용자 목록을 조회하는 API 엔드포인트
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // 2. 이메일 형식 검증이 추가된 사용자 등록 메소드
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        User newUser = userService.registerUser(registrationDto.getEmail(), registrationDto.getPassword(), registrationDto.getName());
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    // 3. 존재하지 않는 사용자 조회 시 에러 처리가 추가된 메소드
    @GetMapping("/{email}")
    public ResponseEntity<User> getUser(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{email}")
    public ResponseEntity<User> updateUser(@PathVariable String email, @RequestBody UserUpdateDto updateDto) {
        User updatedUser = userService.updateUser(email, updateDto.getCurrentPassword(), updateDto.getNewName());
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email, @RequestBody UserDeleteDto deleteDto) {
        userService.deleteUser(email, deleteDto.getPassword());
        return ResponseEntity.noContent().build();
    }



}