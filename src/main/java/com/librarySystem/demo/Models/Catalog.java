package com.librarySystem.demo.Models;

import java.util.List;
import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "catalogs")
public class Catalog {

    @Id
    private String id;
    private String userId;
    private List<String> bookIds;
    private int quantity;
    private Date borrowDate;
    private Date expiredDate;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<String> getBookIds() { return bookIds; }
    public void setBookIds(List<String> bookIds) { this.bookIds = bookIds; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Date getBorrowDate() { return borrowDate; }
    public void setBorrowDate(Date borrowDate) { this.borrowDate = borrowDate; }

    public Date getExpiredDate() { return expiredDate; }
    public void setExpiredDate(Date expiredDate) { this.expiredDate = expiredDate; }
}
