package com.example.tdddemo.service;

import com.example.tdddemo.TodoNotFoundException;
import com.example.tdddemo.entity.Todo;
import com.example.tdddemo.repository.TodoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TodoServiceImpl implements TodoService {

  private final TodoRepository todoRepository;

  @Autowired
  public TodoServiceImpl(TodoRepository todoRepository) {
    this.todoRepository = todoRepository;
  }

  @Override
  public Todo createTodo(String title) {
    Todo todo = new Todo(title);
    return todoRepository.save(todo);
  }

  @Override
  public List<Todo> getAllTodos() {
    return todoRepository.findAll();
  }

  @Override
  public Todo getTodoById(Long id) {
    return todoRepository.findById(id)
        .orElseThrow(() -> new TodoNotFoundException(id));
  }

  @Override
  public Todo updateTodo(Long id, String title) {
    Todo todo = getTodoById(id);
    todo.setTitle(title);
    return todoRepository.save(todo);
  }

  @Override
  public Todo toggleTodoCompleted(Long id) {
    Todo todo = getTodoById(id);
    todo.setCompleted(!todo.isCompleted());
    return todoRepository.save(todo);
  }

  @Override
  public void deleteTodo(Long id) {
    todoRepository.deleteById(id);
  }
}