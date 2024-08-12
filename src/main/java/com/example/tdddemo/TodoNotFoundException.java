package com.example.tdddemo;

public class TodoNotFoundException extends RuntimeException {
  public TodoNotFoundException(Long id) {
    super("Todo not found with id: " + id);
  }
}