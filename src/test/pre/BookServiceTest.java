package com.example.tdddemo;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

public class BookServiceTest {

    @Test
    public void testPurchaseBook() {
        // Mock 객체 생성
        BookRepository mockBookRepo = mock(BookRepository.class);
        PaymentGateway mockPaymentGateway = mock(PaymentGateway.class);

        // Mock 객체의 동작 정의
        when(mockBookRepo.findById("book1")).thenReturn(new Book("book1", 50.0));
        when(mockPaymentGateway.processPayment("user1", 50.0)).thenReturn(true);

        // 테스트할 서비스 객체 생성
        BookService bookService = new BookService(mockBookRepo, mockPaymentGateway);

        // 테스트 실행
        boolean result = bookService.purchaseBook("book1", "user1", 50.0);

        // 결과 확인
        assertTrue(result);

        // Mock 객체의 메소드가 호출되었는지 확인
        verify(mockBookRepo).updateStock("book1");
    }
}