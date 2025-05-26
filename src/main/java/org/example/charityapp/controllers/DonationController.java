package org.example.charityapp.controllers;

import jakarta.validation.Valid;
import org.example.charityapp.DTO.DonationRequest;
import org.example.charityapp.DTO.DonationResponse;
import org.example.charityapp.services.DonationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donations")
public class DonationController {

    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @PostMapping
    public ResponseEntity<DonationResponse> createDonation(@Valid @RequestBody DonationRequest request) {
        DonationResponse response = donationService.createDonation(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DonationResponse>> getAllDonations() {
        List<DonationResponse> donations = donationService.getAllDonations();
        return ResponseEntity.ok(donations);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/validate")
    public ResponseEntity<String> validateDonation(@RequestParam Long donationId) {
        // Implement your validation logic here
        // donationService.validateDonation(donationId);
        return ResponseEntity.ok("Donation validated");
    }
}