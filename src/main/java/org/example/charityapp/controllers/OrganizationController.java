package org.example.charityapp.controllers;

import jakarta.validation.Valid;
import org.example.charityapp.DTO.CharityActionRequest;
import org.example.charityapp.entities.CharityAction;
import org.example.charityapp.entities.Organization;
import org.example.charityapp.entities.User;
import org.example.charityapp.services.CharityActionService;
import org.example.charityapp.services.OrganizationService;
import org.example.charityapp.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/organization")
public class OrganizationController {

    private final OrganizationService organizationService;
    private final UserService userService;
    private final CharityActionService charityActionService;

    public OrganizationController(OrganizationService organizationService, 
                                UserService userService,
                                CharityActionService charityActionService) {
        this.organizationService = organizationService;
        this.userService = userService;
        this.charityActionService = charityActionService;
    }

    /**
     * Display organization dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        
        Organization organization = organizationService.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Organization not found for user"));
        
        model.addAttribute("organization", organization);
        model.addAttribute("charityActions", charityActionService.getCharityActionsByOrganization(organization));
        model.addAttribute("activeTab", "dashboard");
        return "organization/dashboard";
    }
    
    /**
     * Display form to create a new charity action
     */
    @GetMapping("/charity-actions/new")
    public String showCreateCharityActionForm(Model model) {
        model.addAttribute("charityActionRequest", new CharityActionRequest("", "", null, 0.0));
        return "organization/create-charity-action";
    }
    
    /**
     * Handle creation of a new charity action
     */
    @PostMapping("/charity-actions")
    public String createCharityAction(
            @ModelAttribute("charityActionRequest") @Valid CharityActionRequest request,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "organization/create-charity-action";
        }
        
        try {
            User user = userService.getUserByEmail(userDetails.getUsername());
                    
            Organization organization = organizationService.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Organization not found for user"));
            
            // Create and save the charity action
            charityActionService.createCharityAction(new CharityActionRequest(
                request.title(),
                request.description(),
                organization.getId(),
                request.goalAmount()
            ));
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Charity action submitted for approval");
            return "redirect:/organization/dashboard";
            
        } catch (Exception e) {
            bindingResult.reject("error.organization", "Error creating charity action: " + e.getMessage());
            return "organization/create-charity-action";
        }
    }
    
    // API Endpoints (compatible with existing code)
    @ResponseBody
    @PostMapping("/api")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Organization> createOrganization(@RequestBody Organization organization) {
        organizationService.createOrganization(organization);
        return ResponseEntity.ok(organization);
    }

    @ResponseBody
    @GetMapping("/api/{id}")
    public ResponseEntity<Organization> getOrganization(@PathVariable Long id) {
        Organization organization = organizationService.getOrganizationById(id);
        return ResponseEntity.ok(organization);
    }

    @ResponseBody
    @DeleteMapping("/api/{id}/delete")
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long id) {
        try {
            organizationService.deleteOrganization(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}