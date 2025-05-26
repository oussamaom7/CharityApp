package org.example.charityapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.example.charityapp.services.CharityActionService;

@Controller
public class CampaignsPageController {
    @Autowired
    private CharityActionService charityActionService;

    @GetMapping("/campaigns")
    public String campaigns(Model model) {
        model.addAttribute("charityActions", charityActionService.getApprovedCharityActions());
        return "campaigns";
    }
}
