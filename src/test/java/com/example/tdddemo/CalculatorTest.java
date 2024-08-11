package com.example.tdddemo;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("계산기 테스트")
class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    void testAddTwoPositiveNumbers() {
        assertEquals(7, calculator.add(3, 4), "3 + 4 should equal 7");
    }

    @Test
    void testAddTwoNegativeNumbers() {
        assertEquals(-7, calculator.add(-3, -4), "-3 + (-4) should equal -7");
    }

    @Test
    void testAddPositiveAndNegativeNumber() {
        assertEquals(-1, calculator.add(3, -4), "3 + (-4) should equal -1");
    }

    @Test
    void testAddZero() {
        assertEquals(3, calculator.add(3, 0), "3 + 0 should equal 3");
    }

    @Test
    void testAddWithOverflow() {
        assertEquals(Integer.MIN_VALUE, calculator.add(Integer.MAX_VALUE, 1),
            "Adding 1 to MAX_VALUE should result in MIN_VALUE due to overflow");
    }

}