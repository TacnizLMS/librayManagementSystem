package com.librarySystem.demo.Models;

public class CatalogBook {

    private String id;
    private Book book;
    private boolean returnState;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }
    public boolean isReturnState() {
        return returnState;
    }
    public void setReturnState(boolean returnState) {
        this.returnState = returnState;
    }
    
}
