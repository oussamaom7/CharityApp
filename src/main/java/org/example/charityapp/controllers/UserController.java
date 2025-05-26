package org.example.charityapp.controllers;

import org.example.charityapp.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getUserDetails(@PathVariable Long id) {
        String username = userService.getUserDetails(id);
        return ResponseEntity.ok(username);
    }
}