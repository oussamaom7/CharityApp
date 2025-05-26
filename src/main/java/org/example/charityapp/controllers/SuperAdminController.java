package org.example.charityapp.controllers;

import org.example.charityapp.services.UserServiceInterface;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/superadmin/users")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class SuperAdminController {
    private final UserServiceInterface userService;
    public SuperAdminController(UserServiceInterface userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "superadmin/users";
    }

    @PostMapping("/promote")
    public String promoteToAdmin(@RequestParam Long userId) {
        userService.promoteToAdmin(userId);
        return "redirect:/superadmin/users";
    }
    
    @PostMapping("/demote")
    public String demoteToUser(@RequestParam Long userId) {
        userService.demoteToUser(userId);
        return "redirect:/superadmin/users";
    }

}
