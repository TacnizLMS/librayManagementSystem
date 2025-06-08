package com.librarySystem.demo.Models;

public class Book {
    private String id;
    private String title;
    private String author;
    private int quantity;
    private boolean availability;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public boolean getAvailability() { return availability; }
    public void setAvailability(boolean availability) { this.availability = availability; }

}
