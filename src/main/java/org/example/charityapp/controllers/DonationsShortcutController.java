package org.example.charityapp.controllers;

import org.example.charityapp.services.UserBehaviorService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class DonationsShortcutController {
    
    private final UserBehaviorService userBehaviorService;
    
    public DonationsShortcutController(UserBehaviorService userBehaviorService) {
        this.userBehaviorService = userBehaviorService;
    }
    
    @GetMapping("/donations")
    public String showDonations(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            userBehaviorService.trackUserAction(
                (org.example.charityapp.entities.User) auth.getPrincipal(),
                "VIEW_DONATIONS",
                "User viewed donations page",
                request
            );
        } else {
            userBehaviorService.trackAnonymousAction(
                "VIEW_DONATIONS",
                "Anonymous user viewed donations page",
                request
            );
        }
        return "donations";
    }
}
