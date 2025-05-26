package org.example.charityapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.example.charityapp.services.VolunteerService;
import org.example.charityapp.services.UserActionLogService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class VolunteerController {
    private final VolunteerService volunteerService;
    @Autowired
    private UserActionLogService userActionLogService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @GetMapping("/volunteer")
    public String volunteerForm() {
        return "volunteer";
    }

    @PostMapping("/volunteer")
    public String handleVolunteerForm(@RequestParam("name") String name,
                                      @RequestParam("email") String email,
                                      @RequestParam("interest") String interest,
                                      Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) ? auth.getName() : null;
        volunteerService.saveVolunteer(name, email, interest, username);
        if (username != null) {
            userActionLogService.logAction(null, username, "VOLUNTEER_SIGNUP", "Signed up for volunteering: " + interest);
        }
        model.addAttribute("success", true);
        return "volunteer";
    }
}
