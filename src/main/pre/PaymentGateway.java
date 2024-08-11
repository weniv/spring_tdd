package com.example.tdddemo;

public interface PaymentGateway {
    boolean processPayment(String userId, double amount);
}