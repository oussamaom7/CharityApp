package org.example.charityapp.controllers;

import org.example.charityapp.DTO.OrganizationRegistrationRequest;
import org.example.charityapp.entities.Organization;
import org.example.charityapp.entities.User;
import org.example.charityapp.entities.UserRole;
import org.example.charityapp.services.OrganizationService;
import org.example.charityapp.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.Set;

@Controller
@RequestMapping("/register/organization")
public class OrganizationRegistrationController {

    private final UserService userService;
    private final OrganizationService organizationService;
    private final PasswordEncoder passwordEncoder;

    public OrganizationRegistrationController(UserService userService, 
                                            OrganizationService organizationService,
                                            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.organizationService = organizationService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("organizationRegistration", new OrganizationRegistrationRequest(
            "", "", "", "", "", "", "", ""
        ));
        return "organization/register";
    }

    @PostMapping
    public String registerOrganization(
            @Valid @ModelAttribute("organizationRegistration") OrganizationRegistrationRequest request,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            return "organization/register";
        }
        
        // Check if email is already in use
        if (userService.existsByEmail(request.email())) {
            bindingResult.rejectValue("email", "email.exists", "Email is already in use");
            return "organization/register";
        }
        
        try {
            // Create user account
            User user = new User();
            user.setEmail(request.email());
            user.setPassword(passwordEncoder.encode(request.password()));
            user.setRoles(Set.of(UserRole.ROLE_ORGANIZATION));
            user.setEnabled(true);
            user.setCreatedAt(LocalDateTime.now());
            user = userService.save(user);
            // Create organization
            Organization organization = new Organization();
            organization.setName(request.name());
            organization.setDescription(request.description());
            organization.setContactEmail(request.email());
            organization.setContactPhone(request.phone());
            organization.setAddress(request.address());
            organization.setRegistrationNumber(request.registrationNumber());
            organization.setContactPerson(request.contactPerson());
            organization.setStatus("PENDING"); // Requires admin approval
            organization.setRegistrationDate(LocalDateTime.now());
            organization.setUser(user);
            organizationService.createOrganization(organization);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Organization registration submitted successfully! Please wait for admin approval.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            // Most likely a duplicate email or validation error
            bindingResult.reject("registration.error", "Registration failed: " + e.getMessage());
            org.slf4j.LoggerFactory.getLogger(getClass()).error("Registration failed: {}", e.getMessage(), e);
            return "organization/register";
        } catch (Exception e) {
            bindingResult.reject("registration.error", "An unexpected error occurred: " + e.getMessage());
            org.slf4j.LoggerFactory.getLogger(getClass()).error("Unexpected registration error", e);
            return "organization/register";
        }
    }
}
