package com.librarySystem.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.librarySystem.demo.Models.Book;
import com.librarySystem.demo.Models.Type;

public interface TypeRepository extends MongoRepository<Type, String> {
    
   Optional<Type> findByName(String name);

}
