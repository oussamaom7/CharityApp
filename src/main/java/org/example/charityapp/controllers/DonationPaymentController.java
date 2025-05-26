package org.example.charityapp.controllers;

import org.example.charityapp.services.DonationService;
import org.example.charityapp.DTO.DonationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class DonationPaymentController {
    private static final Logger logger = LoggerFactory.getLogger(DonationPaymentController.class);
    private final DonationService donationService;

    @Value("${stripe.public.key}")
    private String stripePublicKey;

    @Autowired
    public DonationPaymentController(DonationService donationService) {
        this.donationService = donationService;
    }

    @GetMapping("/donations/success/{id}")
    public String showSuccess(@PathVariable Long id, Model model) {
        DonationResponse donation = donationService.getDonationById(id);
        model.addAttribute("donation", donation);
        return "donations/payment-success";
    }

    @CrossOrigin
    @PostMapping("/donations/pay/checkout-session")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createStripeCheckoutSession(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Extract and validate payload
            String donorName = (String) payload.get("donorName");
            String charityActionName = (String) payload.get("charityAction");
            String amountStr = String.valueOf(payload.get("amount"));
            
            if (donorName == null || charityActionName == null || amountStr == null) {
                throw new IllegalArgumentException("Missing required fields");
            }

            // Create donation request
            org.example.charityapp.DTO.DonationRequest donationRequest = new org.example.charityapp.DTO.DonationRequest(
                new java.math.BigDecimal(amountStr),
                donorName,
                charityActionName
            );

            // Create donation and get response
            DonationResponse donationResponse = donationService.createDonation(donationRequest);
            
            // Get the Donation entity for Stripe integration
            org.example.charityapp.entities.Donation donation = donationService.findDonationEntityById(donationResponse.id());
            if (donation == null) {
                throw new IllegalStateException("Donation not found after creation");
            }

            // Create Stripe checkout session
            String successUrl = "http://localhost:8080/donations/success/" + donation.getId();
            String cancelUrl = "http://localhost:8080/donations/pay/" + donation.getId();
            String sessionUrl = donationService.getPaymentGateway().createStripeCheckoutSession(donation, successUrl, cancelUrl);

            response.put("sessionUrl", sessionUrl);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to start Stripe Checkout", e);
            response.put("error", "Failed to start Stripe Checkout: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
