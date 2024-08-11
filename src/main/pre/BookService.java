package com.example.tdddemo;

public class BookService {
    private BookRepository bookRepository;
    private PaymentGateway paymentGateway;

    public BookService(BookRepository bookRepository, PaymentGateway paymentGateway) {
        this.bookRepository = bookRepository;
        this.paymentGateway = paymentGateway;
    }

    public boolean purchaseBook(String bookId, String userId, double amount) {
        Book book = bookRepository.findById(bookId);
        if (book != null && book.getPrice() <= amount) {
            boolean paymentSuccess = paymentGateway.processPayment(userId, amount);
            if (paymentSuccess) {
                bookRepository.updateStock(bookId);
                return true;
            }
        }
        return false;
    }
}