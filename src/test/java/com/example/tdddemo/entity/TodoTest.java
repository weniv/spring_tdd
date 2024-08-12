package com.example.tdddemo.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class TodoTest {

    @Test
    public void testTodoCreation() {
        Todo todo = new Todo("Test todo");
        assertThat(todo.getTitle()).isEqualTo("Test todo");
        assertThat(todo.isCompleted()).isFalse();
    }

    @Test
    public void testTodoCompletion() {
        Todo todo = new Todo("Test todo");
        todo.setCompleted(true);
        assertThat(todo.isCompleted()).isTrue();
    }
}