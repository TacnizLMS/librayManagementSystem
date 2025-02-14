package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.library.management.model.Book;

public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findByTitleContaining(String title);
}
