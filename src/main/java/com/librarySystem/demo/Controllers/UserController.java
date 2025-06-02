package com.librarySystem.demo.Controllers;

import java.lang.reflect.Array;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.librarySystem.demo.Models.User;
import com.librarySystem.demo.Repository.UserRepository;
import com.librarySystem.demo.SecurityConfig.JwtProvider;
import com.librarySystem.demo.Services.EmailService;
import com.librarySystem.demo.Services.UserServiceImplementation;
import com.librarySystem.demo.response.AuthResponse;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserServiceImplementation customUserDetails;

    @Autowired
    // private UserService userService;
    private EmailService emailService;

    // @Autowired
    // private JwtProvider jwtProvider;

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        // check email verified or not
        Boolean checkVerification = emailService.checkUserIsVerified(email);
        if (!checkVerification) {
            AuthResponse response = new AuthResponse();
            response.setStatus(false);
            response.setMessage("Please verify your email before logging in.");
            System.out.println("Login Message: " + response.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        Authentication authentication = authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // System.out.println("Sign in authentication" + authentication);
        // Generate JWT token
        String token = JwtProvider.generateToken(authentication);

        // Return token in response
        AuthResponse response = new AuthResponse();
        response.setStatus(true);
        response.setJwt(token);
        System.out.println("JWT Token: " + token);
        response.setMessage("Login success");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getFullName();
        String mobile = user.getMobile();
        String role = user.getRole();

        // Check if email already exists
        if (userRepository.findByEmail(email) != null) {
            AuthResponse conflictResponse = new AuthResponse();
            conflictResponse.setStatus(false);
            conflictResponse.setMessage("Email already registered.");
            return new ResponseEntity<>(conflictResponse, HttpStatus.CONFLICT);
        }

        // Generate verification token
        String verificationToken = UUID.randomUUID().toString();

        // Create and save user
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFullName(fullName);
        newUser.setMobile(mobile);
        newUser.setRole(role);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setVerificationToken(verificationToken);
        newUser.setVerified(false); // Mark as unverified

        // Send verification email
        emailService.sendVerificationEmail(email, verificationToken, role);
        System.out.println("Email sent with token");
        userRepository.save(newUser);
        // Response
        AuthResponse response = new AuthResponse();
        response.setStatus(true);
        response.setMessage("Registration successful! Please verify your email.");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam String token) {
        User user = userRepository.findByVerificationToken(token);
        if (user == null) {
            return new ResponseEntity<>("Invalid verification token", HttpStatus.BAD_REQUEST);
        }

        user.setVerified(true);
        user.setVerificationToken(null); // Clear token after use
        userRepository.save(user);
        // Send email to admin if main admin is verified as an admin
        if (user.getRole().equals("Admin")) {
            System.out.println("Super Admin verified email sent to admin.");
            emailService.AdminVerified(user.getEmail());
        }
        return new ResponseEntity<>("Email verified successfully!", HttpStatus.OK);
    }

    @GetMapping("/get-user")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return new ResponseEntity<>("User not found with email: " + email, HttpStatus.NOT_FOUND);
        }

        // Exclude sensitive info like password and verification token
        user.setPassword(null);
        user.setVerificationToken(null);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) {

        // System.out.println("Email: "+username);
        UserDetails userDetails = customUserDetails.loadUserByUsername(username);

        if (userDetails == null) {
            System.out.println("Sign in details - null" + userDetails);
            throw new BadCredentialsException("Invalid username and password");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            System.out.println("Sign in userDetails - password mismatch" + userDetails);
            throw new BadCredentialsException("Invalid password");
        }
        User dbUser = userRepository.findByEmail(username);
        String userId = dbUser.getId();
        String userRole = dbUser.getRole();
        // System.out.println("Sign in userRole" + userRole);
        // System.out.println("Sign in userId:" + userId);
        String access[] = { userId, userRole };

        return new UsernamePasswordAuthenticationToken(userDetails, access, userDetails.getAuthorities());

    }

}