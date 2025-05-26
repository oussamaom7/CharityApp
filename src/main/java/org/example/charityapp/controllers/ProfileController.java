package org.example.charityapp.controllers;

import org.example.charityapp.DTO.DonationResponse;
import org.example.charityapp.services.DonationService;
import org.example.charityapp.services.VolunteerService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.example.charityapp.services.UserService;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final DonationService donationService;
    private final VolunteerService volunteerService;
    private final UserService userService;

    public ProfileController(DonationService donationService, VolunteerService volunteerService, UserService userService) {
        this.donationService = donationService;
        this.volunteerService = volunteerService;
        this.userService = userService;
    }

    @GetMapping("/donations")
    public String donations(Model model, Principal principal) {
        String username = (principal != null) ? principal.getName() : null;
        List<DonationResponse> donations = (username != null) ? donationService.getUserDonations(username) : java.util.Collections.emptyList();
        model.addAttribute("donations", donations);
        return "profile/donations";
    }

    @GetMapping("/volunteering")
    public String volunteering(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) ? auth.getName() : null;
        model.addAttribute("volunteers", username != null ? volunteerService.getVolunteersByUsername(username) : List.of());
        return "profile/volunteering";
    }

    @GetMapping("/settings")
    public String settings(Model model, Principal principal) {
        String username = principal != null ? principal.getName() : null;
        if (username != null) {
            // Fetch user from UserService (assume bean is available)
            model.addAttribute("user", userService.getUserByUsername(username));
        } else {
            model.addAttribute("user", null);
        }
        return "profile/settings";
    }
}
