package com.example.tdddemo;

public class Book {
  private String id;
  private double price;

  public Book(String id, double price) {
    this.id = id;
    this.price = price;
  }

  public String getId() {
    return id;
  }

  public double getPrice() {
    return price;
  }
}