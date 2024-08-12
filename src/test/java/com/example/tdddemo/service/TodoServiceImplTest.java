package com.example.tdddemo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.tdddemo.entity.Todo;
import com.example.tdddemo.repository.TodoRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TodoServiceImplTest {

  @Mock private TodoRepository todoRepository;

  @InjectMocks private TodoServiceImpl todoService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCreateTodo() {
    Todo todo = new Todo("New Todo");
    when(todoRepository.save(any(Todo.class))).thenReturn(todo);

    Todo createdTodo = todoService.createTodo("New Todo");

    assertThat(createdTodo.getTitle()).isEqualTo("New Todo");
    verify(todoRepository, times(1)).save(any(Todo.class));
  }

  @Test
  void testGetAllTodos() {
    List<Todo> todos = Arrays.asList(new Todo("Todo 1"), new Todo("Todo 2"));
    when(todoRepository.findAll()).thenReturn(todos);

    List<Todo> result = todoService.getAllTodos();

    assertThat(result).hasSize(2);
    assertThat(result.get(0).getTitle()).isEqualTo("Todo 1");
    assertThat(result.get(1).getTitle()).isEqualTo("Todo 2");
  }

  @Test
  void testGetTodoById() {
    Todo todo = new Todo("Test Todo");
    todo.setId(1L);
    when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

    Todo result = todoService.getTodoById(1L);

    assertThat(result).isNotNull();
    assertThat(result.getTitle()).isEqualTo("Test Todo");
  }

  @Test
  void testUpdateTodo() {
    Todo todo = new Todo("Old Title");
    todo.setId(1L);
    when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
    when(todoRepository.save(any(Todo.class))).thenAnswer(i -> i.getArguments()[0]);

    Todo updatedTodo = todoService.updateTodo(1L, "New Title");

    assertThat(updatedTodo.getTitle()).isEqualTo("New Title");
    verify(todoRepository, times(1)).save(any(Todo.class));
  }

  @Test
  void testToggleTodoCompleted() {
    Todo todo = new Todo("Test Todo");
    todo.setId(1L);
    todo.setCompleted(false);
    when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
    when(todoRepository.save(any(Todo.class))).thenAnswer(i -> i.getArguments()[0]);

    Todo toggledTodo = todoService.toggleTodoCompleted(1L);

    assertThat(toggledTodo.isCompleted()).isTrue();
    verify(todoRepository, times(1)).save(any(Todo.class));
  }

  @Test
  void testDeleteTodo() {
    todoService.deleteTodo(1L);

    verify(todoRepository, times(1)).deleteById(1L);
  }
}