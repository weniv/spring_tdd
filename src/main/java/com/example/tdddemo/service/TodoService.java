package com.example.tdddemo.service;

import com.example.tdddemo.entity.Todo;
import java.util.List;

public interface TodoService {
  Todo createTodo(String title);

  List<Todo> getAllTodos();

  Todo getTodoById(Long id);

  Todo updateTodo(Long id, String title);

  Todo toggleTodoCompleted(Long id);

  void deleteTodo(Long id);
}