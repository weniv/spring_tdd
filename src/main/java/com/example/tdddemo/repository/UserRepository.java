package com.example.tdddemo.repository;

import com.example.tdddemo.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  User findByEmail(String email);

  // 이메일 존재 여부를 확인하는 메서드 추가
  boolean existsByEmail(String email);
}