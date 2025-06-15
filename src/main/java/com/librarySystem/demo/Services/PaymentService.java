// PaymentService.java
package com.librarySystem.demo.Services;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    public String createCheckoutSession(String productName, Long amount, String description) throws Exception {
        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams.LineItem.PriceData.ProductData.Builder productDataBuilder = 
            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(productName);
        
        // Add description if provided
        if (description != null && !description.trim().isEmpty()) {
            productDataBuilder.setDescription(description);
        }

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/success")
                .setCancelUrl("http://localhost:8080/cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("lkr")  // Changed to LKR
                                                .setUnitAmount(amount)  // Dynamic amount
                                                .setProductData(productDataBuilder.build())
                                                .build())
                                .build())
                .build();

        Session session = Session.create(params);
        return session.getUrl();
    }
}
