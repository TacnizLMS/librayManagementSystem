// PaymentController.java
package com.librarySystem.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.librarySystem.demo.Dto.PaymentRequestDTO;
import com.librarySystem.demo.Dto.FinePayBookIdDTO;
import com.librarySystem.demo.Services.PaymentService;
import com.librarySystem.demo.Services.CatalogService;
import com.librarySystem.demo.Models.Catalog;

@RestController
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private CatalogService catalogService;

    @PostMapping("/pay")
    public ResponseEntity<String> makePayment(@RequestBody PaymentRequestDTO paymentRequest) throws Exception {
        String sessionUrl = paymentService.createCheckoutSession(
            paymentRequest.getProductName(),
            paymentRequest.getAmount(),
            paymentRequest.getDescription()
        );
        return ResponseEntity.ok(sessionUrl);
    }

    // Pay full catalog fine - initiate payment
    @PostMapping("/pay-catalog-fine/{id}")
    public ResponseEntity<String> initiateCatalogFinePayment(@PathVariable String id) throws Exception {
        String sessionUrl = paymentService.createCatalogFinePaymentSession(id);
        return ResponseEntity.ok(sessionUrl);
    }

    // Pay single catalog book fine - initiate payment
    @PostMapping("/pay-catalog-book-fine/{id}")
    public ResponseEntity<String> initiateCatalogBookFinePayment(
            @PathVariable String id, 
            @RequestBody FinePayBookIdDTO request) throws Exception {
        String sessionUrl = paymentService.createCatalogBookFinePaymentSession(id, request.getCatalogBookId());
        return ResponseEntity.ok(sessionUrl);
    }

    // Success callback for catalog fine payment
    @GetMapping("/success/catalog-fine/{catalogId}")
    public ResponseEntity<String> catalogFinePaymentSuccess(@PathVariable String catalogId) {
        Catalog updatedCatalog = catalogService.payCatalogFine(catalogId);
        if (updatedCatalog != null) {
            return ResponseEntity.ok("✅ Catalog fine payment was successful! Catalog ID: " + catalogId);
        } else {
            return ResponseEntity.badRequest().body("❌ Failed to process catalog fine payment. Please check the Catalog ID.");
        }
    }

    // Success callback for catalog book fine payment
    @GetMapping("/success/catalog-book-fine/{catalogId}/{catalogBookId}")
    public ResponseEntity<String> catalogBookFinePaymentSuccess(
            @PathVariable String catalogId, 
            @PathVariable String catalogBookId) {
        Catalog updatedCatalog = catalogService.payCatalogBookFine(catalogId, catalogBookId);
        if (updatedCatalog != null) {
            return ResponseEntity.ok("✅ Catalog book fine payment was successful! Catalog ID: " + catalogId + ", Book ID: " + catalogBookId);
        } else {
            return ResponseEntity.badRequest().body("❌ Failed to process catalog book fine payment. Please check the Catalog ID and Book ID.");
        }
    }

    // Pay single catalog book fine from cash
    @PostMapping("/pay-catalog-book-fine-cash/{id}")
    public ResponseEntity<String> payCatalogBookFineCash(
            @PathVariable String id, 
            @RequestBody FinePayBookIdDTO request) {
        Catalog updatedCatalog = catalogService.payCatalogBookFine(id, request.getCatalogBookId());
        if (updatedCatalog != null) {
            return ResponseEntity.ok("Catalog book fine paid successfully with cash! Catalog ID: " + id + ", Book ID: " + request.getCatalogBookId());
        } else {
            return ResponseEntity.badRequest().body("Failed to process catalog book fine payment with cash. Please check the Catalog ID and Book ID.");
        }
    }

    // pay full catalog fine from cash
    @PostMapping("/pay-catalog-fine-cash/{id}")
    public ResponseEntity<String> payCatalogFineCash(@PathVariable String id) {
        Catalog updatedCatalog = catalogService.payCatalogFine(id);
        if (updatedCatalog != null) {
            return ResponseEntity.ok("Catalog fine paid successfully with cash! Catalog ID: " + id);
        } else {
            return ResponseEntity.badRequest().body("Failed to process catalog fine payment with cash. Please check the Catalog ID.");
        }
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