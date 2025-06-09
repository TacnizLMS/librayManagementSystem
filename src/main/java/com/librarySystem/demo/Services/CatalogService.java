package com.librarySystem.demo.Services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.librarySystem.demo.Dto.CatalogBookDTO;
import com.librarySystem.demo.Dto.CatalogRequestDTO;
import com.librarySystem.demo.Exception.AlreadyExistsException;
import com.librarySystem.demo.Exception.NotFoundException;
import com.librarySystem.demo.Models.Book;
import com.librarySystem.demo.Models.Catalog;
import com.librarySystem.demo.Models.CatalogBook;
import com.librarySystem.demo.Repository.BookRepository;
import com.librarySystem.demo.Repository.CatalogRepository;

@Service
public class CatalogService {
    @Autowired
    private CatalogRepository catalogRepository;
    @Autowired
    private BookRepository bookRepository;

    public List<Catalog> getAllCatalogs() {
        return catalogRepository.findAll();
    }

    public Optional<Catalog> getCatalogById(String id) {
        return catalogRepository.findById(id);
    }

    public List<Catalog> getAllCatalogByUserId(String userId) {
        return catalogRepository.findByUserId(userId);
    }

    public Catalog addCatalog(CatalogRequestDTO request) {
        Catalog catalog = new Catalog();
        catalog.setUserId(request.getUserId());

        List<CatalogBook> catalogBooks = new ArrayList<>();
        int totalQuantity = 0;

        for (CatalogBookDTO bookReq : request.getBooks()) {
            // Check if the book exists
            Book book = bookRepository.findById(bookReq.getBookId())
                    .orElseThrow(() -> new NotFoundException("Book not found with id: " + bookReq.getBookId()));
            // check if the book is available
            if (!book.getAvailability()) {
                throw new RuntimeException("Book with ID " + bookReq.getBookId() + " is not available now.");
            }

            for (int i = 0; i < bookReq.getQuantity(); i++) {
                CatalogBook catalogBook = new CatalogBook();
                catalogBook.setId(UUID.randomUUID().toString());
                catalogBook.setBook(book);
                catalogBook.setReturnState(false);
                catalogBooks.add(catalogBook);
            }

            totalQuantity += bookReq.getQuantity();
        }

        catalog.setCatalogBooks(catalogBooks);
        catalog.setQuantity(totalQuantity);
        catalog.setBorrowDate(new Date());

        // Example: set expiredDate to 14 days from now
        Calendar cal = Calendar.getInstance();
        cal.setTime(catalog.getBorrowDate());
        cal.add(Calendar.DAY_OF_MONTH, 14);
        catalog.setExpiredDate(cal.getTime());

        catalog.setCompleteState(false);

        return catalogRepository.save(catalog);
    }

    public Catalog updateCatalog(String id, CatalogRequestDTO request) {
        return catalogRepository.findById(id).map(catalog -> {
            List<CatalogBook> catalogBooks = catalog.getCatalogBooks();
            // check allready returned
            if (catalog.isCompleteState()) {
                throw new AlreadyExistsException("Catalog with id " + id + " is already returned.");
            }
            // Case 1: If completeState is true, mark all as returned
            if (Boolean.TRUE.equals(request.isCompleteState())) {

                for (CatalogBook cb : catalogBooks) {
                    cb.setReturnState(true);
                }
                catalog.setCompleteState(true);
            }

            // Case 2: Update only selected books
            if (request.getBooks() != null && !request.getBooks().isEmpty()) {
                for (CatalogBookDTO bookRequest : request.getBooks()) {
                    int quantityToUpdate = bookRequest.getQuantity();

                    for (CatalogBook cb : catalogBooks) {
                        if (!cb.isReturnState() &&
                                cb.getBook().getId().equals(bookRequest.getBookId()) &&
                                quantityToUpdate > 0) {
                            cb.setReturnState(true);
                            quantityToUpdate--;
                        }
                    }
                }

                // If all catalog books are returned, mark completeState true
                boolean allReturned = catalogBooks.stream().allMatch(CatalogBook::isReturnState);
                catalog.setCompleteState(allReturned);
            }

            return catalogRepository.save(catalog);
        }).orElseThrow(() -> new RuntimeException("Catalog not found"));
    }

    public void deleteCatalog(String id) {
        catalogRepository.deleteById(id);
    }
}
