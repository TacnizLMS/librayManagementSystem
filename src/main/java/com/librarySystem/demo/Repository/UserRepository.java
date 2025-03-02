package com.librarySystem.demo.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.librarySystem.demo.Models.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByResetToken(String token);
}
