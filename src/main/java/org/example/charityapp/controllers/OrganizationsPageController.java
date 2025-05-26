package org.example.charityapp.controllers;

import org.example.charityapp.entities.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.example.charityapp.services.OrganizationService;
import java.util.List;

@Controller
public class OrganizationsPageController {
    @Autowired
    private OrganizationService organizationService;

    @GetMapping("/organizations")
    public String organizations(Model model) {
        List<Organization> organizations = organizationService.getApprovedOrganizations();
        if (organizations == null) {
            organizations = java.util.Collections.emptyList();
        }
        model.addAttribute("organizations", organizations);
        return "organizations";
    }
}
