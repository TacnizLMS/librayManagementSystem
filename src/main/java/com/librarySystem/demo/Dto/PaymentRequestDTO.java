package com.librarySystem.demo.Dto;

public class PaymentRequestDTO {
    private String productName;
    private Long amount;
    private String description;

    // constructors, getters, and setters...
    public PaymentRequestDTO() {
    }

    public PaymentRequestDTO(String productName, Long amount, String description) {
        this.productName = productName;
        this.amount = amount;
        this.description = description;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
