package com.librarySystem.demo.Dto;

public class PaymentRequestDTO {
    private String productName;
    private Double amount;
    private String description;

    // Constructors
    public PaymentRequestDTO() {}

    public PaymentRequestDTO(String productName, Double amount, String description) {
        this.productName = productName;
        this.amount = amount;
        this.description = description;
    }

    // Getters and Setters
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}