package com.example.tdddemo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class StringUtilsTest {

  @ParameterizedTest
  @CsvSource({"hello,olleh", "world,dlrow", "'',''", "a,a"})
  void testReverse(String input, String expected) {
    assertEquals(expected, StringUtils.reverse(input));
  }

  @ParameterizedTest
  @CsvSource({"'A man a plan a canal Panama',true",
      "'race a car',true",
      "hello,false",
      "'',true"})
  void testIsPalindrome(String input, boolean expected) {
    assertEquals(expected, StringUtils.isPalindrome(input));
  }
}