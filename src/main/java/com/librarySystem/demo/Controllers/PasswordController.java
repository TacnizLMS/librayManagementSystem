package com.librarySystem.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.librarySystem.demo.Models.User;
import com.librarySystem.demo.Repository.UserRepository;
import com.librarySystem.demo.Services.EmailService;
import com.librarySystem.demo.response.AuthResponse;

@RestController
@RequestMapping("/password")
public class PasswordController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    // private UserService userService;
    private EmailService emailService;
    //password change handler
    @PostMapping("/changePassword")
    public ResponseEntity<AuthResponse> changePassword(@RequestParam String email, @RequestParam String oldPassword, @RequestParam String newPassword) {
        User user = userRepository.findByEmail(email);
        
        if (user == null) {
            AuthResponse response = new AuthResponse();
            response.setStatus(false);
            response.setMessage("User not found.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            AuthResponse response = new AuthResponse();
            response.setStatus(false);
            response.setMessage("Current password is incorrect.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        AuthResponse response = new AuthResponse();
        response.setStatus(true);
        response.setMessage("Password changed successfully.");
        emailService.sendVerificationEmail(response.getMessage(), email);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    
}
