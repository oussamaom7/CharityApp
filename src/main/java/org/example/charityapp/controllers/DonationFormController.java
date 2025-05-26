package org.example.charityapp.controllers;

import org.example.charityapp.DTO.DonationRequest;
import org.example.charityapp.services.DonationService;
import org.example.charityapp.services.UserActionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;
import java.security.Principal;

@Controller
public class DonationFormController {
    private final DonationService donationService;
    private final UserActionLogService userActionLogService;

    @Autowired
    public DonationFormController(DonationService donationService, UserActionLogService userActionLogService) {
        this.donationService = donationService;
        this.userActionLogService = userActionLogService;
    }

    @GetMapping("/donate-form")
    public String showDonationForm(Model model) {
        // Optionally add charityActions if needed for the form
        return "Donation";
    }

    @PostMapping("/donate-form")
    public String handleDonationForm(
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("donorName") String donorName,
            @RequestParam("charityActionName") String charityActionName,
            Model model,
            Principal principal) {
        DonationRequest req = new DonationRequest(amount, donorName, charityActionName);
        try {
            var response = donationService.createDonation(req);
            if ("FAILED".equals(response.status())) {
                model.addAttribute("error", "Payment failed. Please try again or use a different payment method.");
                if (principal != null) {
                    userActionLogService.logAction(null, principal.getName(), "DONATION_FAILED", "Donation failed for amount: " + req.getAmount());
                }
                return "Donation";
            }
            model.addAttribute("success", true);
            if (principal != null) {
                userActionLogService.logAction(null, principal.getName(), "DONATION_SUCCESS", "Donation submitted: " + amount);
            }
            // Redirect to payment form for the new donation
            return "redirect:/donations/pay/" + response.id();
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while processing your donation: " + e.getMessage());
            if (principal != null) {
                userActionLogService.logAction(null, principal.getName(), "DONATION_ERROR", e.getMessage());
            }
            return "Donation";
        }
    }
}
