package com.example.tdddemo;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
}