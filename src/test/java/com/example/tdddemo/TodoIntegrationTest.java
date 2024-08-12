package com.example.tdddemo;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.tdddemo.controller.TodoRequest;
import com.example.tdddemo.entity.Todo;
import com.example.tdddemo.repository.TodoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class TodoIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private TodoRepository todoRepository;

  @BeforeEach
  void setUp() {
    todoRepository.deleteAll();
  }

  @Test
  public void testTodoLifecycle() throws Exception {
    // 1. Create a new todo
    TodoRequest newTodo = new TodoRequest("Buy groceries");
    String createdTodoJson =
        mockMvc
            .perform(
                post("/api/todos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf())
                    .content(objectMapper.writeValueAsString(newTodo)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title", is("Buy groceries")))
            .andExpect(jsonPath("$.completed", is(false)))
            .andReturn()
            .getResponse()
            .getContentAsString();

    Todo createdTodo = objectMapper.readValue(createdTodoJson, Todo.class);

    // 2. Get all todos
    mockMvc
        .perform(get("/api/todos").with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].title", is("Buy groceries")));

    // 3. Get todo by id
    mockMvc
        .perform(get("/api/todos/" + createdTodo.getId()).with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title", is("Buy groceries")));

    // 4. Update todo
    TodoRequest updateRequest = new TodoRequest("Buy groceries and cleaning supplies");
    mockMvc
        .perform(
            put("/api/todos/" + createdTodo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title", is("Buy groceries and cleaning supplies")));

    // 5. Toggle todo completed
    mockMvc
        .perform(patch("/api/todos/" + createdTodo.getId() + "/toggle").with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.completed", is(true)));

    // 6. Delete todo
    mockMvc.perform(delete("/api/todos/" + createdTodo.getId()).with(csrf())).andExpect(status().isNoContent());

    // 7. Verify todo is deleted
    mockMvc.perform(get("/api/todos/" + createdTodo.getId()).with(csrf()))
        .andExpect(status().isNotFound())
        .andExpect(content().string(containsString("Todo not found with id: " + createdTodo.getId())));
  }
}