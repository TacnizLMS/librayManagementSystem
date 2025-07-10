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
            message.setText(String.format("Click the following link to verify %s as admin of LMS: %s", toEmail, link));
            message.setSubject("Library Account Verification");
            toEmail = "librarymstacniz@gmail.com";  // Change this to when you want to send to super admin
        }
        else {
            System.out.println("Sending email to user");
            message.setText("Click the following link to verify your email in LMS: " + link);
            message.setSubject("Library Account Verification");
        }
        message.setTo(toEmail);        

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

    public void AdminVerified(String toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Admin Permission Approvel");
        message.setText("Your email has been verified by the Super Admin.");
        System.out.println("Message: " + message.getText());

        mailSender.send(message);
    }

    public void sendVerificationEmail(String message , String toEmail) {
        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(toEmail);
        emailMessage.setSubject("LMS Notification");
        emailMessage.setText(message);

        mailSender.send(emailMessage);

    }
}
