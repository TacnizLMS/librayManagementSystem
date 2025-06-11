package com.librarySystem.demo.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.librarySystem.demo.Dto.BookRequestDTO;
import com.librarySystem.demo.Exception.AlreadyExistsException;
import com.librarySystem.demo.Exception.NotFoundException;
import com.librarySystem.demo.Models.Book;
import com.librarySystem.demo.Repository.BookRepository;
import com.librarySystem.demo.Repository.TypeRepository;
import com.librarySystem.demo.Models.Type;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private TypeRepository typeRepository; // ✅ Add this line

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(String id) {
        return bookRepository.findById(id);
    }

    public Optional<Book> getBookByName(String title) {
        return bookRepository.findByTitle(title);
    }

    public Book addBookFromDTO(BookRequestDTO dto) {
        Optional<Book> existingBook = bookRepository.findByTitle(dto.getTitle());
        if (existingBook.isPresent()) {
            throw new AlreadyExistsException("Book with title '" + dto.getTitle() + "' already exists.");
        }

        Type type = typeRepository.findById(dto.getTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid typeId"));

        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setType(type);
        book.setQuantity(dto.getQuantity());
        book.setAvailability(dto.isAvailability());

        return bookRepository.save(book);
    }

    public Book updateBook(String id, BookRequestDTO bookDetails) {
        return bookRepository.findById(id).map(book -> {
            // Quantity update
            if (bookDetails.getQuantity() != 0) {
                int updatedQuantity = book.getQuantity() + bookDetails.getQuantity();
                book.setQuantity(Math.max(updatedQuantity, 0));
            }

            // Title and Author
            if (bookDetails.getTitle() != null) {
                book.setTitle(bookDetails.getTitle());
            }
            if (bookDetails.getAuthor() != null) {
                book.setAuthor(bookDetails.getAuthor());
            }

            // ✅ Update Type using typeId
            if (bookDetails.getTypeId() != null) {
                Type type = typeRepository.findById(bookDetails.getTypeId())
                        .orElseThrow(() -> new NotFoundException("Type not found with id: " + bookDetails.getTypeId()));
                book.setType(type);
            }

            // Availability
            book.setAvailability(bookDetails.isAvailability());

            return bookRepository.save(book);
        }).orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }

}
