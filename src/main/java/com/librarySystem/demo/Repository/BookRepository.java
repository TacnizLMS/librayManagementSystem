package com.librarySystem.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.librarySystem.demo.Models.Book;
public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findByTitleContaining(String title);

   Optional<Book> findByTitle(String title);

}
