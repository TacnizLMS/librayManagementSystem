// PaymentController.java
package com.librarySystem.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.librarySystem.demo.Dto.PaymentRequestDTO;
import com.librarySystem.demo.Services.PaymentService;

@RestController
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/pay")
    public ResponseEntity<String> makePayment(@RequestBody PaymentRequestDTO paymentRequest) throws Exception {
        String sessionUrl = paymentService.createCheckoutSession(
            paymentRequest.getProductName(),
            paymentRequest.getAmount(),
            paymentRequest.getDescription()
        );
        return ResponseEntity.ok(sessionUrl);
    }

    @GetMapping("/success")
    public ResponseEntity<String> paymentSuccess() {
        return ResponseEntity.ok("✅ Payment was successful!");
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> paymentCancelled() {
        return ResponseEntity.ok("❌ Payment was cancelled.");
    }
}