// PaymentService.java
package com.librarySystem.demo.Services;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.librarySystem.demo.Exception.UnprocessableEntityException;
import com.librarySystem.demo.Exception.NotFoundException;
import com.librarySystem.demo.Models.Catalog;
import com.librarySystem.demo.Models.CatalogBook;
import com.librarySystem.demo.Repository.CatalogRepository;

@Service
public class PaymentService {

        @Value("${stripe.secret.key}")
        private String stripeSecretKey;

        @Autowired
        private CatalogRepository catalogRepository;

        private static final Long MIN_LKR_IN_CENTS = 15200L;
        private static final String BASE_URL = "https://libraymanagementsystem-production-9bbb.up.railway.app";

        public String createCheckoutSession(String productName, Double amount, String description) throws Exception {
                Stripe.apiKey = stripeSecretKey;
                Long amountInCents = Math.round(amount * 100);
                validateAmount(amountInCents);

                return createStripeSession(
                                productName,
                                description,
                                amountInCents,
                                BASE_URL + "/success",
                                BASE_URL + "/cancel");
        }

        public String createCatalogFinePaymentSession(String catalogId) throws Exception {
                Stripe.apiKey = stripeSecretKey;

                Catalog catalog = catalogRepository.findById(catalogId)
                                .orElseThrow(() -> new NotFoundException("Catalog not found with id: " + catalogId));

                double totalFine = catalog.getCatalogBooks().stream()
                                .filter(cb -> !cb.isFinePaid() && cb.getFine() > 0)
                                .mapToDouble(CatalogBook::getFine)
                                .sum();

                if (totalFine <= 0) {
                        throw new IllegalArgumentException("No unpaid fines found for this catalog");
                }

                Long amountInCents = Math.round(totalFine * 100);
                validateAmount(amountInCents);

                return createStripeSession(
                                "Library Catalog Fine Payment",
                                "Payment for all unpaid fines in catalog: " + catalogId,
                                amountInCents,
                                BASE_URL + "/success/catalog-fine/" + catalogId,
                                BASE_URL + "/cancel");
        }

        public String createCatalogBookFinePaymentSession(String catalogId, String catalogBookId) throws Exception {
                Stripe.apiKey = stripeSecretKey;

                Catalog catalog = catalogRepository.findById(catalogId)
                                .orElseThrow(() -> new NotFoundException("Catalog not found with id: " + catalogId));

                CatalogBook catalogBook = catalog.getCatalogBooks().stream()
                                .filter(cb -> cb.getId().equals(catalogBookId))
                                .findFirst()
                                .orElseThrow(() -> new NotFoundException(
                                                "CatalogBook not found with id: " + catalogBookId));

                if (catalogBook.isFinePaid() || catalogBook.getFine() <= 0) {
                        throw new UnprocessableEntityException(catalogBookId + " is already paid or has no fine.");
                }

                Long amountInCents = Math.round(catalogBook.getFine() * 100);
                validateAmount(amountInCents);

                return createStripeSession(
                                "Library Book Fine Payment",
                                "Fine payment for book in catalog: " + catalogId,
                                amountInCents,
                                BASE_URL +"/success/catalog-book-fine/" + catalogId + "/" + catalogBookId,
                                BASE_URL +"/cancel");
        }

        //  Reusable Stripe Session Creation
        private String createStripeSession(String productName, String description, Long amountInCents,
                        String successUrl, String cancelUrl) throws Exception {
                SessionCreateParams.LineItem.PriceData.ProductData.Builder productDataBuilder = SessionCreateParams.LineItem.PriceData.ProductData
                                .builder()
                                .setName(productName);

                if (description != null && !description.trim().isEmpty()) {
                        productDataBuilder.setDescription(description);
                }

                SessionCreateParams params = SessionCreateParams.builder()
                                .setMode(SessionCreateParams.Mode.PAYMENT)
                                .setSuccessUrl(successUrl)
                                .setCancelUrl(cancelUrl)
                                .addLineItem(
                                                SessionCreateParams.LineItem.builder()
                                                                .setQuantity(1L)
                                                                .setPriceData(
                                                                                SessionCreateParams.LineItem.PriceData
                                                                                                .builder()
                                                                                                .setCurrency("lkr")
                                                                                                .setUnitAmount(amountInCents)
                                                                                                .setProductData(productDataBuilder
                                                                                                                .build())
                                                                                                .build())
                                                                .build())
                                .build();

                Session session = Session.create(params);
                return session.getUrl();
        }

        // Validate Amount
        private void validateAmount(Long amountInCents) {
                if (amountInCents < MIN_LKR_IN_CENTS) {
                        throw new UnprocessableEntityException("Minimum fine amount is 152 LKR");
                }
        }
}
