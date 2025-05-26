package org.example.charityapp.controllers;

import org.example.charityapp.entities.CharityAction;
import org.example.charityapp.entities.User;
import org.example.charityapp.services.DonationService;
import org.example.charityapp.services.OrganizationService;
import org.example.charityapp.services.CharityActionService;
import org.example.charityapp.services.VolunteerService;
import org.example.charityapp.services.UserService;
import org.example.charityapp.services.UserActionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final DonationService donationService;
    private final OrganizationService organizationService;
    private final CharityActionService charityActionService;
    private final VolunteerService volunteerService;
    private final UserService userService;
    private final UserActionLogService userActionLogService;

    @Autowired
    public AdminController(DonationService donationService, OrganizationService organizationService, CharityActionService charityActionService, VolunteerService volunteerService, UserService userService, UserActionLogService userActionLogService) {
        this.donationService = donationService;
        this.organizationService = organizationService;
        this.charityActionService = charityActionService;
        this.volunteerService = volunteerService;
        this.userService = userService;
        this.userActionLogService = userActionLogService;
    }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        User user = userService.getAllUsers().stream()
                .filter(u -> u.getUsername().equals(principal.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        model.addAttribute("pendingCharityActions", charityActionService.getPendingCharityActions());
        model.addAttribute("recentActions", userActionLogService.getRecentLogs(10));
        return "admin/profile";
    }
    
    // Approval endpoints have been removed as organizations and charity actions are now automatically approved

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        model.addAttribute("totalOrganizations", organizationService.getAllOrganizations().size());
        model.addAttribute("totalCharityActions", charityActionService.getAllCharityActions().size());
        model.addAttribute("totalDonations", donationService.getAllDonations().size());
        model.addAttribute("recentActions", userActionLogService.getRecentLogs(10));
        model.addAttribute("donations", donationService.getAllDonations());
        model.addAttribute("organizations", organizationService.getAllOrganizations());
        model.addAttribute("charityActions", charityActionService.getCharityActionsForApprovedOrganizations());
        return "admin/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "redirect:/admin";
    }

    @GetMapping("/volunteering")
    public String allVolunteering(Model model) {
        model.addAttribute("volunteers", volunteerService.getAllVolunteers());
        return "admin/volunteering";
    }

    @PostMapping("/simulate-transaction")
    public String simulateTransaction(RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) ? auth.getName() : "admin";
        userActionLogService.logAction(null, username, "SIMULATE_TRANSACTION", "Simulated donation: 99.99");
        redirectAttributes.addFlashAttribute("toast", "Simulated transaction logged!");
        return "redirect:/admin/dashboard";
    }
}
