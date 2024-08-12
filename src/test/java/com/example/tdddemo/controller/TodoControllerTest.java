package com.example.tdddemo.controller;

import com.example.tdddemo.entity.Todo;
import com.example.tdddemo.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
@WithMockUser
public class TodoControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private TodoService todoService;

  @Autowired private ObjectMapper objectMapper;

  @Test
  public void testCreateTodo() throws Exception {
    Todo todo = new Todo("New Todo");
    todo.setId(1L);
    when(todoService.createTodo(any(String.class))).thenReturn(todo);

    mockMvc
        .perform(
            post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(new TodoRequest("New Todo"))))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.title").value("New Todo"))
        .andExpect(jsonPath("$.completed").value(false));
  }

  @Test
  public void testGetAllTodos() throws Exception {
    when(todoService.getAllTodos())
        .thenReturn(Arrays.asList(new Todo("Todo 1"), new Todo("Todo 2")));

    mockMvc
        .perform(get("/api/todos").with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].title").value("Todo 1"))
        .andExpect(jsonPath("$[1].title").value("Todo 2"));
  }

  @Test
  public void testGetTodoById() throws Exception {
    Todo todo = new Todo("Test Todo");
    todo.setId(1L);
    when(todoService.getTodoById(1L)).thenReturn(todo);

    mockMvc
        .perform(get("/api/todos/1").with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.title").value("Test Todo"));
  }

  @Test
  public void testUpdateTodo() throws Exception {
    Todo updatedTodo = new Todo("Updated Todo");
    updatedTodo.setId(1L);
    when(todoService.updateTodo(anyLong(), any(String.class))).thenReturn(updatedTodo);

    mockMvc
        .perform(
            put("/api/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(new TodoRequest("Updated Todo"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.title").value("Updated Todo"));
  }

  @Test
  public void testToggleTodoCompleted() throws Exception {
    Todo toggledTodo = new Todo("Toggled Todo");
    toggledTodo.setId(1L);
    toggledTodo.setCompleted(true);
    when(todoService.toggleTodoCompleted(1L)).thenReturn(toggledTodo);

    mockMvc
        .perform(patch("/api/todos/1/toggle").with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.completed").value(true));
  }

  @Test
  public void testDeleteTodo() throws Exception {
    mockMvc.perform(delete("/api/todos/1").with(csrf())).andExpect(status().isNoContent());
  }
}