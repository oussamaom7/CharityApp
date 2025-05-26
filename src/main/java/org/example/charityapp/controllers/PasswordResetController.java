package org.example.charityapp.controllers;

import org.example.charityapp.entities.User;
import org.example.charityapp.repositories.UserRepository;
import org.example.charityapp.services.EmailService;
import org.example.charityapp.services.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/password-reset")
public class PasswordResetController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordResetService passwordResetService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/request")
    public ResponseEntity<?> requestReset(@RequestParam String email) {
        Optional<User> userOpt = userRepository.findByUsername(email); // Assuming username is email
        if (userOpt.isPresent()) {
            String token = passwordResetService.createPasswordResetToken(userOpt.get());
            // Send email with token link
            String resetLink = "http://localhost:8080/password-reset/confirm?token=" + token;
            String subject = "Password Reset Request";
            String text = "To reset your password, click the following link: " + resetLink + "\nIf you did not request a password reset, you can ignore this email.";
            emailService.sendSimpleMessage(email, subject, text);
            return ResponseEntity.ok("Password reset link sent.");
        } else {
            return ResponseEntity.badRequest().body("No user with that email.");
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmReset(@RequestParam String token, @RequestParam String newPassword) {
        Optional<User> userOpt = passwordResetService.validatePasswordResetToken(token);
        if (userOpt.isPresent()) {
            passwordResetService.updatePassword(userOpt.get(), newPassword);
            return ResponseEntity.ok("Password updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }
    }
}
