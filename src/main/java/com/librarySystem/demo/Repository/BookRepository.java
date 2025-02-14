package com.librarySystem.demo.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.librarySystem.demo.Models.Book;
public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findByTitleContaining(String title);
}
