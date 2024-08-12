package com.example.tdddemo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.tdddemo.entity.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TodoRepositoryTest {

  @Autowired private TodoRepository todoRepository;

  @Test
  public void testSaveTodo() {
    Todo todo = new Todo("Test todo");
    Todo savedTodo = todoRepository.save(todo);
    assertThat(savedTodo.getId()).isNotNull();
    assertThat(savedTodo.getTitle()).isEqualTo("Test todo");
  }

  @Test
  public void testFindTodoById() {
    Todo todo = new Todo("Test todo");
    todoRepository.save(todo);
    Todo foundTodo = todoRepository.findById(todo.getId()).orElse(null);
    assertThat(foundTodo).isNotNull();
    assertThat(foundTodo.getTitle()).isEqualTo("Test todo");
  }
}