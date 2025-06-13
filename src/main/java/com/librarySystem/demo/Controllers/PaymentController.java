package com.librarySystem.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.librarySystem.demo.Services.PaymentService;

@RestController
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/pay")
    public ResponseEntity<String> makePayment() throws Exception {
        String sessionUrl = paymentService.createCheckoutSession();
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
