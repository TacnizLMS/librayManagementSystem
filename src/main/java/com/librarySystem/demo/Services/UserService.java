package com.librarySystem.demo.Services;

import java.util.List;

import com.librarySystem.demo.Models.User;

public interface UserService {

    List<User> getAllUser();

    User findUserProfileByJwt(String jwt);

    User findUserByEmail(String email);

    User findUserById(String userId);

    List<User> findAllUsers();

    // ✅ New methods for email verification
    User findByVerificationToken(String token);

    User updateUser(User user);

    
}

