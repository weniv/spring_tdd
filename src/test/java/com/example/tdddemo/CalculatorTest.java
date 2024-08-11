package com.example.tdddemo;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("계산기 테스트")
class CalculatorTest {

    private Calculator calculator;

    @BeforeAll
    static void setUpAll() {
        System.out.println("@BeforeAll - 모든 테스트 시작 전 1번 실행");
    }

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
        System.out.println("@BeforeEach - 각 테스트 시작 전 실행");
    }

    @Test
    @DisplayName("1 더하기 1은 2여야 한다")
    void testAddition() {
        assertEquals(2, calculator.add(1, 1), "1 + 1 should equal 2");
    }

    @Test
    @DisplayName("5 빼기 3은 2여야 한다")
    void testSubtraction() {
        assertEquals(2, calculator.subtract(5, 3), "5 - 3 should equal 2");
    }

    @Test
    @Disabled("곱하기 기능은 아직 구현되지 않았습니다")
    void testMultiplication() {
        assertEquals(6, calculator.multiply(2, 3), "2 * 3 should equal 6");
    }

    @AfterEach
    void tearDown() {
        System.out.println("@AfterEach - 각 테스트 종료 후 실행");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("@AfterAll - 모든 테스트 종료 후 1번 실행");
    }
}