package org.example.charityapp.controllers;

import org.example.charityapp.DTO.CharityActionRequest;
import org.example.charityapp.services.CharityActionService;
import org.example.charityapp.services.OrganizationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/charity-actions")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCharityActionController {
    // This service contains all the business logic for managing charity actions
    private final CharityActionService charityActionService;
    // This service lets us fetch and manage organizations
    private final OrganizationService organizationService;

    // When we create this controller, let's make sure it knows how to talk to the right services
    public AdminCharityActionController(CharityActionService charityActionService, OrganizationService organizationService) {
        this.charityActionService = charityActionService;
        this.organizationService = organizationService;
    }

    /**
     * Show the form to add a new charity action.
     * We also fetch all approved organizations so the admin can pick which one the action belongs to.
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        // Let's get all the organizations that are approved, so we only show valid options
        var organizations = organizationService.getApprovedOrganizations();
        model.addAttribute("organizations", organizations);
        // Pre-fill the form with empty values (or pick the first org if possible)
        model.addAttribute("charityActionRequest", new CharityActionRequest("", "", organizations.isEmpty() ? null : organizations.get(0).getId(), 1.0));
        return "admin/add-charity-action";
    }

    /**
     * Handle the submission of the new charity action form.
     * After creating the action, we send the admin back to the main admin page.
     */
    @PostMapping("/add")
    public String handleAddForm(@ModelAttribute CharityActionRequest charityActionRequest, Model model) {
        charityActionService.createCharityAction(charityActionRequest);
        return "redirect:/admin";
    }

    // Charity actions created by admin are automatically active

    /**
     * Show a list of all charity actions so the admin can manage them.
     */
    @GetMapping
    public String listCharityActions(Model model) {
        model.addAttribute("charityActions", charityActionService.getAllCharityActions());
        return "admin/charity-actions";
    }

    @PostMapping("/approve")
    public String approveCharityAction(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            charityActionService.approveCharityAction(id);
            redirectAttributes.addFlashAttribute("successMessage", "Charity action approved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error approving charity action: " + e.getMessage());
        }
        return "redirect:/admin/charity-actions";
    }

    @PostMapping("/reject")
    public String rejectCharityAction(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            charityActionService.rejectCharityAction(id);
            redirectAttributes.addFlashAttribute("successMessage", "Charity action rejected successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error rejecting charity action: " + e.getMessage());
        }
        return "redirect:/admin/charity-actions";
    }
}
