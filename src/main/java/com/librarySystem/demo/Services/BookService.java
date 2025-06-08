package com.librarySystem.demo.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.librarySystem.demo.Models.Book;
import com.librarySystem.demo.Repository.BookRepository;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(String id) {
        return bookRepository.findById(id);
    }

    public Optional<Book> getBookByName(String title) {
        return bookRepository.findByTitle(title);
    }

    public Book addBook(Book book) {
        //check if the book already exists by title
        Optional<Book> existingBook = bookRepository.findByTitle(book.getTitle());
        if (existingBook.isPresent()) {
            // If the book exists return as message
            throw new RuntimeException("Book with title '" + book.getTitle() + "' already exists.");      
        }
        return bookRepository.save(book);
    }

    public Book updateBook(String id, Book bookDetails) {
        return bookRepository.findById(id).map(book -> {
            // Update quantity by adding the new value to existing quantity
            if (bookDetails.getQuantity() != 0) {
                int updatedQuantity = book.getQuantity() + bookDetails.getQuantity();
                book.setQuantity(Math.max(updatedQuantity, 0)); // Prevent negative quantity if needed
            }
            // Optional: update other fields only if needed
            if (bookDetails.getTitle() != null) {
                book.setTitle(bookDetails.getTitle());
            }
            if (bookDetails.getAuthor() != null) {
                book.setAuthor(bookDetails.getAuthor());
            }
            if (bookDetails.getAvailability() != false) {
                book.setAvailability(bookDetails.getAvailability());
            }

            return bookRepository.save(book);
        }).orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }
}
