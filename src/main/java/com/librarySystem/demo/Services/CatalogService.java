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
import com.librarySystem.demo.Dto.FinePayBookIdDTO;
import com.librarySystem.demo.Exception.AlreadyExistsException;
import com.librarySystem.demo.Exception.BadRequestException;
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
                throw new RuntimeException(
                        "Book with ID " + bookReq.getBookId() + " is not available currently in Library.");
            }

            if (bookReq.getQuantity() > book.getAvailableCount()) {
                throw new BadRequestException(
                        "Requested quantity for book with ID " + bookReq.getBookId() + " not available now.");
            }

            for (int i = 0; i < bookReq.getQuantity(); i++) {
                CatalogBook catalogBook = new CatalogBook();
                catalogBook.setId(UUID.randomUUID().toString());
                catalogBook.setBook(book);
                catalogBook.setFine(0.0);
                catalogBook.setFinePaid(false);
                catalogBook.setReturnState(false);
                catalogBooks.add(catalogBook);
            }
            // update book available count
            book.setAvailableCount(book.getAvailableCount() - bookReq.getQuantity());
            // update in db
            bookRepository.save(book);

            totalQuantity += bookReq.getQuantity();
        }

        catalog.setCatalogBooks(catalogBooks);
        catalog.setQuantity(totalQuantity);
        catalog.setBorrowDate(null);

        catalog.setExpiredDate(null);

        catalog.setCompleteState("pending"); // pending borrow complete

        return catalogRepository.save(catalog);
    }

    public Catalog updateCatalog(String id, CatalogRequestDTO request) {
        return catalogRepository.findById(id).map(catalog -> {
            List<CatalogBook> catalogBooks = catalog.getCatalogBooks();
            // check allready returned
            if ("complete".equals(catalog.getCompleteState())) {
                throw new AlreadyExistsException("Catalog with id " + id + " is already returned.");
            }
            if ("borrow".equals(request.getCompleteState())) {
                catalog.setBorrowDate(new Date());
                // Example: set expiredDate to 14 days from now
                Calendar cal = Calendar.getInstance();
                cal.setTime(catalog.getBorrowDate());
                cal.add(Calendar.DAY_OF_MONTH, 14);
                catalog.setExpiredDate(cal.getTime());

                catalog.setCompleteState("borrow"); // pending borrow complete

            }
            // Case 1: If completeState is true, mark all as returned
            if ("complete".equals(request.getCompleteState())) {
                for (CatalogBook cb : catalogBooks) {
                    // calculate fine charge
                    if (!cb.isReturnState()) {
                        long diffInMillies = new Date().getTime() - catalog.getExpiredDate().getTime();
                        long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);
                        if (diffInDays > 0) {
                            cb.setFine(cb.getFine() + (diffInDays * 20)); // Assuming a fine of 5 per day
                        }
                        // get book id
                        String bookId = cb.getBook().getId();
                        // get book by id and update available count
                        Book updatedBook = bookRepository.findById(bookId)
                                .orElseThrow(() -> new NotFoundException("The book has removed under id: " + bookId));
                        updatedBook.setAvailableCount(updatedBook.getAvailableCount() + 1);
                        bookRepository.save(updatedBook);
                    }
                    cb.setReturnState(true);
                }
                catalog.setCompleteState("complete");
            }

            // Case 2: Update only selected books
            if ("borrow".equals(catalog.getCompleteState()) && request.getBooks() != null) {
                System.out.println("Updating catalog with id: ");
                for (CatalogBookDTO bookRequest : request.getBooks()) {
                    int quantityToUpdate = bookRequest.getQuantity();

                    for (CatalogBook cb : catalogBooks) {
                        if (!cb.isReturnState() &&
                                cb.getBook().getId().equals(bookRequest.getBookId()) &&
                                quantityToUpdate > 0) {
                            // Calculate fine charge if not already returned
                            if (cb.getFine() == 0.0) {
                                long diffInMillies = new Date().getTime() - catalog.getExpiredDate().getTime();
                                long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);
                                if (diffInDays > 0) {
                                    cb.setFine(cb.getFine() + (diffInDays * 5)); // Assuming a fine of 5 per day
                                }
                            }
                            // get book id
                            String bookId = cb.getBook().getId();
                            // get book by id and update available count
                            Book updatedBook = bookRepository.findById(bookId)
                                    .orElseThrow(() -> new NotFoundException("Book not found with id: " + bookId));
                            updatedBook.setAvailableCount(updatedBook.getAvailableCount() + 1);
                            bookRepository.save(updatedBook);
                            cb.setReturnState(true);
                            quantityToUpdate--;
                        }
                    }
                }

                // If all catalog books are returned, mark completeState true
                boolean allReturned = catalogBooks.stream().allMatch(CatalogBook::isReturnState);
                if (allReturned) {
                    catalog.setCompleteState("complete");
                }
            } else if ("pending".equals(catalog.getCompleteState())) {
                throw new BadRequestException("Catalog with id " + id + " is not in borrow state.");
            }

            return catalogRepository.save(catalog);
        }).orElseThrow(() -> new RuntimeException("Catalog not found"));
    }

    public Catalog returnBackCatalog(String id) {
        return catalogRepository.findById(id).map(catalog -> {
            // check allready returned
            if ("complete".equals(catalog.getCompleteState())) {
                // mark as return back
                catalog.setCompleteState("borrow");
                for (CatalogBook cb : catalog.getCatalogBooks()) {
                    cb.setReturnState(false);
                    cb.setFine(0.0);
                }
            }
            return catalogRepository.save(catalog);
        }).orElseThrow(() -> new NotFoundException("Catalog not found with id: " + id));
    }

    public Catalog returnBackCatalogBook(String catalogId, FinePayBookIdDTO request) {
        String catalogBookId = request.getCatalogBookId();

        // Fetch the catalog by ID
        Catalog catalog = catalogRepository.findById(catalogId)
                .orElseThrow(() -> new NotFoundException("Catalog not found with id: " + catalogId));

        // Find the catalogBook inside the catalog
        CatalogBook catalogBook = catalog.getCatalogBooks().stream()
                .filter(cb -> cb.getId().equals(catalogBookId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("CatalogBook not found with id: " + catalogBookId));

        // If already returned, revert
        if (catalogBook.isReturnState()) {
            System.out.println("Reverting return for CatalogBook with id: " + catalogBookId);
            catalogBook.setReturnState(false);
            catalogBook.setFine(0.0);

            // If the catalog was marked complete, revert it
            if ("complete".equals(catalog.getCompleteState())) {
                catalog.setCompleteState("borrow");
            }
        } else {
            System.out.println("CatalogBook with id: " + catalogBookId + " is not in return state.");
            throw new BadRequestException("CatalogBook with id " + catalogBookId + " is not in return state.");
        }

        return catalogRepository.save(catalog);
    }

    public Catalog payCatalogFine(String CatalogId) {
        // get all catalogbooks and mark as pay fine by getCatalogBooks
        Catalog catalog = catalogRepository.findById(CatalogId)
                .orElseThrow(() -> new NotFoundException("Catalog not found with id: " + CatalogId));
        // check status = complete
        if (!"complete".equals(catalog.getCompleteState())) {
            throw new BadRequestException("Catalog with id " + CatalogId + " is not in complete state.");
        }
        List<CatalogBook> catalogBooks = catalog.getCatalogBooks();
        boolean paidAnyFine = false;
        for (CatalogBook cb : catalogBooks) {
            if (!cb.isFinePaid() && cb.getFine() > 0) {
                cb.setFinePaid(true);
                paidAnyFine = true;
            }
        }
        if (!paidAnyFine) {
            throw new BadRequestException("No unpaid fines to pay for this catalog.");
        }
        catalog.setCatalogBooks(catalogBooks);
        return catalogRepository.save(catalog);
    }

    public Catalog payCatalogBookFine(String catalogId, String catalogBookId) {
        // get catalog by id
        Catalog catalog = catalogRepository.findById(catalogId)
                .orElseThrow(() -> new NotFoundException("Catalog not found with id: " + catalogId));
        // get catalog book by id
        CatalogBook catalogBook = catalog.getCatalogBooks().stream()
                .filter(cb -> cb.getId().equals(catalogBookId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("CatalogBook not found with id: " + catalogBookId));
        // check catalog state is complete
        if (!"complete".equals(catalog.getCompleteState())) {
            throw new BadRequestException("Catalog with id " + catalogId + " is not in complete state.");
        }
        // mark as pay fine
        boolean paidAnyFine = false;
        if (!catalogBook.isFinePaid() && catalogBook.getFine() > 0) {
            catalogBook.setFinePaid(true);
            paidAnyFine = true;
        }
        if (!paidAnyFine) {
            throw new BadRequestException("No unpaid fines to pay for this catalog.");
        }

        return catalogRepository.save(catalog);
    }

    public void deleteCatalog(String id) {
        catalogRepository.deleteById(id);
    }

    public double getFinesByUserId(String userId) {
        List<Catalog> catalogs = catalogRepository.findByUserId(userId);
        double totalFine = 0.0;

        for (Catalog catalog : catalogs) {
            for (CatalogBook catalogBook : catalog.getCatalogBooks()) {
                if (!catalogBook.isFinePaid() && catalogBook.getFine() > 0) {
                    totalFine += catalogBook.getFine();
                }
            }
        }

        return totalFine;
    }
}
