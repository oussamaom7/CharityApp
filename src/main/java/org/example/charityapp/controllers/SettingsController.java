package org.example.charityapp.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SettingsController {

    @GetMapping("/settings")
    @PreAuthorize("isAuthenticated()")
    public String settings(Model model) {
        return "settings";
    }

    @PostMapping("/settings/update-profile")
    @PreAuthorize("isAuthenticated()")
    public String updateProfile(@RequestParam String email,
                              @RequestParam(required = false) String currentPassword,
                              @RequestParam(required = false) String newPassword,
                              RedirectAttributes redirectAttributes) {
        // TODO: Implement profile update logic
        redirectAttributes.addFlashAttribute("success", "Profile updated successfully");
        return "redirect:/settings";
    }
} 