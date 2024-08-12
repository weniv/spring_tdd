package com.example.tdddemo.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

public class UserDeleteDto {
  private String password;

  // 기본 생성자
  public UserDeleteDto() {}

  public UserDeleteDto(String password) {
    this.password = password;
  }

  // getter와 setter
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}