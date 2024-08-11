package com.example.tdddemo;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.tdddemo.entity.User;
import org.junit.jupiter.api.Test;

public class UserTest {
    @Test
    public void testUserCreation() {
        User user = new User("john@example.com", "password123", "John Doe");

        assertThat(user.getEmail()).isEqualTo("john@example.com");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.getName()).isEqualTo("John Doe");
        assertThat(user.getId()).isNull();
    }
}