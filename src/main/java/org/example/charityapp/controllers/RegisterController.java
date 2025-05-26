package org.example.charityapp.controllers;

import org.example.charityapp.DTO.RegisterRequest;
import org.example.charityapp.services.AuthService;
import org.example.charityapp.services.UserActionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
public class RegisterController {

    private final AuthService authService;
    private final UserActionLogService userActionLogService;

    @Autowired
    public RegisterController(AuthService authService, UserActionLogService userActionLogService) {
        this.authService = authService;
        this.userActionLogService = userActionLogService;
    }

    @GetMapping({"/register", "/Register"})
    public String registerForm(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            return "redirect:/home";
        }
        // Initialize with empty organization fields
        model.addAttribute("registerRequest", new RegisterRequest("", "", "", "", "USER", "", "", ""));
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerRequest") RegisterRequest registerRequest,
                         BindingResult result, Model model, 
                         org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        // Validate passwords match
        if (!registerRequest.password().equals(registerRequest.confirmPassword())) {
            result.rejectValue("confirmPassword", "error.confirmPassword", "Passwords do not match");
        }
        
        // If organization registration, validate organization fields
        if ("ORGANIZATION".equals(registerRequest.role())) {
            if (registerRequest.organizationName() == null || registerRequest.organizationName().trim().isEmpty()) {
                result.rejectValue("organizationName", "error.organizationName", "Organization name is required");
            }
            if (registerRequest.registrationNumber() == null || registerRequest.registrationNumber().trim().isEmpty()) {
                result.rejectValue("registrationNumber", "error.registrationNumber", "Registration number is required");
            }
        }
        
        if (result.hasErrors()) {
            return "register";
        }

        try {
            String message = authService.register(registerRequest);
            userActionLogService.logAction(null, registerRequest.username(), "REGISTER_SUCCESS", 
                "ORGANIZATION".equals(registerRequest.role()) ? "Organization registration submitted" : "User registered successfully");
            
            redirectAttributes.addFlashAttribute("toastMessage", message);
            return "redirect:/login";
        } catch (Exception e) {
            userActionLogService.logAction(null, registerRequest.username(), "REGISTER_FAILED", e.getMessage());
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }
}