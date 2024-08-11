package com.example.tdddemo;

public interface BookRepository {
    Book findById(String id);
    void updateStock(String id);
}