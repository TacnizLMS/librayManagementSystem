package com.librarySystem.demo.Dto;

import java.util.List;

public class CatalogRequestDTO {
    private String userId;
    private List<CatalogBookDTO> books;
    private String completeState;// pending borrow complete

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public List<CatalogBookDTO> getBooks() {
        return books;
    }
    public void setBooks(List<CatalogBookDTO> books) {
        this.books = books;
    }
    public String getCompleteState() {
        return completeState;
    }
    public void setCompleteState(String completeState) {
        this.completeState = completeState;
    }

}



