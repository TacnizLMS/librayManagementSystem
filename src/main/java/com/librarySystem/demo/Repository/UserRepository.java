package com.example.demo.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.library.management.model.User;

public interface UserRepository extends MongoRepository<User, String> {
}
