package org.example.charityapp.controllers;

import org.example.charityapp.entities.User;
import org.example.charityapp.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {
    private final UserService userService;
    public AdminUserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }
    @GetMapping("/add")
    public String showAddUserForm(Model model) {
        // You may want to use a DTO for user creation, for now use a simple User object
        model.addAttribute("user", new User());
        return "admin/add-user";
    }
    @PostMapping("/add")
    public String handleAddUserForm(@ModelAttribute("user") User user, Model model) {
        userService.createUser(user);
        model.addAttribute("success", "User added successfully!");
        return "redirect:/admin/users";
    }
}
