package com.librarySystem.demo.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.librarySystem.demo.Dto.BookRequestDTO;
import com.librarySystem.demo.Models.Book;
import com.librarySystem.demo.Services.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Optional<Book> getBookById(@PathVariable String id) {
        return bookService.getBookById(id);
    }

    @GetMapping("/search/{title}")
    public Optional<Book> getBookByName(@PathVariable String title) {
        return bookService.getBookByName(title);
    }

    @PostMapping
    public Book addBook(@RequestBody BookRequestDTO dto) {
        Book addedBook = bookService.addBookFromDTO(dto);
        System.out.println("Book added successfully");
        return addedBook;
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable String id, @RequestBody BookRequestDTO bookDetails) {
        return bookService.updateBook(id, bookDetails);
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
        return "Book removed successfully";
    }
    
}
