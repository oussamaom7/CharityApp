package org.example.charityapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.example.charityapp.services.CharityActionService;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class DonateController {
    private final CharityActionService charityActionService;

    @Autowired
    public DonateController(CharityActionService charityActionService) {
        this.charityActionService = charityActionService;
    }

    @GetMapping("/donate")
    public String donate(Model model) {
        model.addAttribute("charityActions", charityActionService.getAllCharityActions());
        return "Donation";
    }
}
