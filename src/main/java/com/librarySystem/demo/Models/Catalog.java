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
    private List<BookStationary> bookStationaries;
    private int quantity;
    private Date borrowDate;
    private Date expiredDate;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<BookStationary> getBookStationaries() { return bookStationaries; }
    public void setBookStationaries(List<BookStationary> bookStationaries) { this.bookStationaries = bookStationaries; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Date getBorrowDate() { return borrowDate; }
    public void setBorrowDate(Date borrowDate) { this.borrowDate = borrowDate; }

    public Date getExpiredDate() { return expiredDate; }
    public void setExpiredDate(Date expiredDate) { this.expiredDate = expiredDate; }

    // Inner class for BookStationary
    public static class BookStationary {
        private String bookId;
        private String bookName;
        private int bookEachQuantity;

        public String getBookId() { return bookId; }
        public void setBookId(String bookId) { this.bookId = bookId; }

        public String getBookName() { return bookName; }
        public void setBookName(String bookName) { this.bookName = bookName; }

        public int getBookEachQuantity() { return bookEachQuantity; }
        public void setBookEachQuantity(int bookEachQuantity) { this.bookEachQuantity = bookEachQuantity; }
    }
}
