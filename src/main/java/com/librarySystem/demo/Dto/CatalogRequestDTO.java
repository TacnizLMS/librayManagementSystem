package com.librarySystem.demo.Dto;

import java.util.List;

public class CatalogRequestDTO {
    private String userId;
    private List<CatalogBookDTO> books;
    private boolean completeState;

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
    public boolean isCompleteState() {
        return completeState;
    }
    public void setCompleteState(boolean completeState) {
        this.completeState = completeState;
    }

}



