package org.example.charityapp.controllers;

import org.example.charityapp.entities.User;
import org.example.charityapp.repositories.UserRepository;
import org.example.charityapp.services.EmailService;
import org.example.charityapp.services.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class ForgotPasswordController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordResetService passwordResetService;
    @Autowired
    private EmailService emailService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam("email") String email, Model model) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            String token = passwordResetService.createPasswordResetToken(userOpt.get());
            String resetLink = "http://localhost:8080/reset-password?token=" + token;
            String subject = "Password Reset Request";
            String text = "To reset your password, click the following link: " + resetLink + "\nIf you did not request a password reset, you can ignore this email.";
            emailService.sendSimpleMessage(email, subject, text);
            model.addAttribute("message", "Password reset link has been sent to your email.");
        } else {
            model.addAttribute("message", "No user found with that email address.");
        }
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam("token") String token,
                                      @RequestParam("newPassword") String newPassword,
                                      Model model) {
        Optional<User> userOpt = passwordResetService.validatePasswordResetToken(token);
        if (userOpt.isPresent()) {
            passwordResetService.updatePassword(userOpt.get(), newPassword);
            model.addAttribute("message", "Password updated successfully. You can now log in.");
        } else {
            model.addAttribute("message", "Invalid or expired token.");
        }
        return "reset-password";
    }
}
