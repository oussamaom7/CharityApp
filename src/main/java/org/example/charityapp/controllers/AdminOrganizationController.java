package org.example.charityapp.controllers;

import org.example.charityapp.entities.Organization;
import org.example.charityapp.services.OrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/organizations")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrganizationController {
    private static final Logger logger = LoggerFactory.getLogger(AdminOrganizationController.class);
    private final OrganizationService organizationService;
    public AdminOrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }
    @GetMapping("/add")
    public String showAddForm(Model model) {
        // Use the no-arg constructor and set fields in the form
        model.addAttribute("organization", new Organization());
        return "admin/add-organization";
    }
    @PostMapping("/add")
    public String handleAddForm(@ModelAttribute Organization organization, RedirectAttributes redirectAttributes) {
        try {
            logger.info("Received organization add request: {}", organization);
            
            // Ensure name is not null or blank
            String name = organization.getName();
            if (name == null || name.trim().isEmpty()) {
                logger.warn("Organization name is empty");
                return "redirect:/admin/organizations/add?error=name";
            }
            
            // Create a new organization with required fields
            Organization newOrg = new Organization();
            newOrg.setName(name.trim());
            newOrg.setStatus("APPROVED");
            newOrg.setRegistrationDate(java.time.LocalDateTime.now());
            
            // Set optional fields if provided
            if (organization.getDescription() != null) {
                newOrg.setDescription(organization.getDescription().trim());
            }
            
            logger.info("Saving organization: {}", newOrg);
            organizationService.createOrganization(newOrg);
            logger.info("Organization saved successfully");
            
            // Add a success flash attribute and redirect to the organizations list with a success parameter
            redirectAttributes.addFlashAttribute("successMessage", "Organization created successfully!");
            return "redirect:/admin/organizations?success";
        } catch (Exception e) {
            logger.error("Error saving organization: ", e);
            return "redirect:/admin/organizations/add?error=server";
        }
    }
    @GetMapping
    public String listOrganizations(Model model) {
        model.addAttribute("organizations", organizationService.getAllOrganizations());
        return "admin/organizations";
    }
}
