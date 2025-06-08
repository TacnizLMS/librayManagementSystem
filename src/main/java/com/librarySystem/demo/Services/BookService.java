package com.librarySystem.demo.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.librarySystem.demo.Exception.AlreadyExistsException;
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
        Optional<Book> existingBook = bookRepository.findByTitle(book.getTitle());
        if (existingBook.isPresent()) {
           throw new AlreadyExistsException("Book with title '" + book.getTitle() + "' already exists.");
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
            if (bookDetails.getType() != null) {
                book.setType(bookDetails.getType());
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
