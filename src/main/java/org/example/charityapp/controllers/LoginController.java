package org.example.charityapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginRequest", new LoginRequest("", ""));
        // Si un toastMessage a été transmis via FlashAttribute, il sera déjà dans le modèle
        return "login";
    }

    @GetMapping("/Login")
    public String loginCapital(Model model) {
        model.addAttribute("loginRequest", new LoginRequest("", ""));
        // Si un toastMessage a été transmis via FlashAttribute, il sera déjà dans le modèle
        return "login";
    }

    public record LoginRequest(String username, String password) {}
}