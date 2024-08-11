package com.example.tdddemo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class SimpleTest {

  @Test
  void 간단한_덧셈_테스트() {
    int result = 1 + 1;
    assertEquals(3, result, "1 + 1 should equal 2");
  }
}