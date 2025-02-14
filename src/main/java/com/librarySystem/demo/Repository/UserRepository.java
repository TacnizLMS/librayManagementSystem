package com.librarySystem.demo.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.librarySystem.demo.Models.User;

public interface UserRepository extends MongoRepository<User, String> {
}
