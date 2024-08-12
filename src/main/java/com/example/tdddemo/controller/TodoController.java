package com.example.tdddemo.controller;

import com.example.tdddemo.TodoNotFoundException;
import com.example.tdddemo.entity.Todo;
import com.example.tdddemo.service.TodoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

  private final TodoService todoService;

  @Autowired
  public TodoController(TodoService todoService) {
    this.todoService = todoService;
  }

  @PostMapping
  public ResponseEntity<Todo> createTodo(@RequestBody TodoRequest todoRequest) {
    Todo createdTodo = todoService.createTodo(todoRequest.getTitle());
    return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<Todo>> getAllTodos() {
    List<Todo> todos = todoService.getAllTodos();
    return ResponseEntity.ok(todos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
    Todo todo = todoService.getTodoById(id);
    return ResponseEntity.ok(todo);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Todo> updateTodo(
      @PathVariable Long id, @RequestBody TodoRequest todoRequest) {
    Todo updatedTodo = todoService.updateTodo(id, todoRequest.getTitle());
    return ResponseEntity.ok(updatedTodo);
  }

  @PatchMapping("/{id}/toggle")
  public ResponseEntity<Todo> toggleTodoCompleted(@PathVariable Long id) {
    Todo toggledTodo = todoService.toggleTodoCompleted(id);
    return ResponseEntity.ok(toggledTodo);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
    todoService.deleteTodo(id);
    return ResponseEntity.noContent().build();
  }

  @ExceptionHandler(TodoNotFoundException.class)
  public ResponseEntity<String> handleTodoNotFoundException(TodoNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }
}