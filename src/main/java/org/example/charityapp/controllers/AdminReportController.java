package org.example.charityapp.controllers;

import org.example.charityapp.services.DonationService;
import org.example.charityapp.services.VolunteerService;
import org.example.charityapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/reports")
@PreAuthorize("hasRole('ADMIN')")
public class AdminReportController {
    @Autowired
    private DonationService donationService;
    @Autowired
    private VolunteerService volunteerService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String reports(Model model) {
        try {
            model.addAttribute("totalDonations", donationService.getTotalDonationsAmount());
            model.addAttribute("totalVolunteers", volunteerService.getTotalVolunteersCount());
            model.addAttribute("totalUsers", userService.getTotalUsersCount());
            model.addAttribute("recentDonations", donationService.getRecentDonations(5));
            model.addAttribute("recentVolunteers", volunteerService.getRecentVolunteers(5));
            return "admin/reports";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.toString());
            model.addAttribute("stackTrace", e.getStackTrace());
            return "admin/reports-error";
        }
    }
}
