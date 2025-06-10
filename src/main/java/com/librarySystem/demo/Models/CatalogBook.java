package com.librarySystem.demo.Models;

public class CatalogBook {

    private String id;
    private Book book;
    private double fine;
    private boolean finePaid;
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
    public double getFine() {
        return fine;
    }
    public void setFine(double fine) {
        this.fine = fine;
    }
    public boolean isFinePaid() {
        return finePaid;
    }
    public void setFinePaid(boolean finePaid) {
        this.finePaid = finePaid;
    }
    public boolean isReturnState() {
        return returnState;
    }
    public void setReturnState(boolean returnState) {
        this.returnState = returnState;
    }
    
}
