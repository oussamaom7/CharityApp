package org.example.charityapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.example.charityapp.repositories.BlogRepository;
import org.example.charityapp.services.CharityActionService;

@Controller
public class HomeController {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CharityActionService charityActionService;

    @GetMapping("/home")
    public String index(Model model) {
        model.addAttribute("page", "home");
        model.addAttribute("organizations", java.util.Collections.emptyList());
        model.addAttribute("charityActions", charityActionService.getAllCharityActions());
        model.addAttribute("blogs", blogRepository.findAll());
        return "Home";
    }

    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("page", "hello");
        model.addAttribute("message", "Hello World");
        return "Home";
    }

    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/home";
    }
}
