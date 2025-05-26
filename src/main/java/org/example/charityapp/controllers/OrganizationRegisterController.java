package org.example.charityapp.controllers;

import org.example.charityapp.entities.Organization;
import org.example.charityapp.services.OrganizationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register-organization")
public class OrganizationRegisterController {
    private final OrganizationService organizationService;
    public OrganizationRegisterController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }
    @GetMapping
    public String showRegisterForm(Model model) {
        model.addAttribute("organization", new Organization(""));
        return "register-organization";
    }
    @PostMapping
    public String handleRegisterForm(@ModelAttribute Organization organization, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        organization.setStatus("PENDING");
        organizationService.createOrganization(organization);
        redirectAttributes.addFlashAttribute("toastMessage", "Votre demande d'inscription d'organisation est en attente de validation.");
        return "redirect:/login";
    }
}
