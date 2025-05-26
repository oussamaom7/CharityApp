package org.example.charityapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DonationPageController {
    @GetMapping("/donation")
    public String donation(Model model) {
        return "Donation";
    }
}
