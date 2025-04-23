package com.librarySystem.demo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.librarySystem.demo.Models.User;
import com.librarySystem.demo.Repository.UserRepository;
import com.librarySystem.demo.response.AuthResponse;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;
    

    public void sendVerificationEmail(String toEmail, String token, String role) {
        String link = "http://localhost:8080/auth/verify?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        if (role.equals("Admin")) {
            System.out.println("Sending email to admin");
            toEmail = "librarymstacniz@gmail.com";  // Change this to your desired email
        }
        message.setTo(toEmail);
        message.setSubject("Library Account Verification");
        message.setText("Click the following link to verify your email: " + link);

        mailSender.send(message);
    }

    public boolean checkUserIsVerified(String email) {
        User dbUser = userRepository.findByEmail(email);
        if (dbUser == null) {
            AuthResponse response = new AuthResponse();
            response.setStatus(false);
            response.setMessage("Invalid email or password.");
            return false;
        }
        if (!dbUser.isVerified()) {
            AuthResponse response = new AuthResponse();
            response.setStatus(false);
            return false;
        }
        return true;
    }
}
