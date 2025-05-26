package org.example.charityapp.controllers;

import org.example.charityapp.services.DonationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin/donations")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDonationController {
    private final DonationService donationService;
    public AdminDonationController(DonationService donationService) {
        this.donationService = donationService;
    }
    @GetMapping
    public String listDonations(Model model) {
        model.addAttribute("donations", donationService.getAllDonations());
        return "admin/donations";
    }

    @GetMapping("/approve/{id}")
    public String approveDonation(@PathVariable Long id) {
        donationService.approveDonation(id);
        return "redirect:/admin?toast=donation_approved";
    }

    @GetMapping("/reject/{id}")
    public String rejectDonation(@PathVariable Long id) {
        donationService.rejectDonation(id);
        return "redirect:/admin?toast=donation_rejected";
    }

    @GetMapping("/add")
    public String showAddDonationForm(Model model) {
        model.addAttribute("charityActions", donationService.getAllCharityActions());
        return "admin/add-donation";
    }

    @PostMapping("/add")
    public String handleAddDonation(
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("donorName") String donorName,
            @RequestParam("charityActionId") Long charityActionId,
            Model model) {
        donationService.createDonationFromAdmin(amount, donorName, charityActionId);
        return "redirect:/admin/donations";
    }
}
