package org.example.charityapp.services;

import jakarta.validation.Valid;
import org.example.charityapp.DTO.RegisterRequest;
import org.example.charityapp.entities.Organization;
import org.example.charityapp.entities.User;
import org.example.charityapp.repositories.UserRepository;
import org.example.charityapp.services.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final OrganizationService organizationService;

    public AuthService(UserRepository userRepository, 
                      BCryptPasswordEncoder passwordEncoder, 
                      JwtUtil jwtUtil,
                      OrganizationService organizationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.organizationService = organizationService;
    }

    public String register(@Valid RegisterRequest request) {
        // Check if username already exists
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Check if email already exists
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Determine roles based on registration type
        Set<org.example.charityapp.entities.UserRole> roles;
        if ("ROLE_ORGANIZATION".equals(request.role())) {
            // Validate organization fields
            if (request.organizationName() == null || request.organizationName().trim().isEmpty()) {
                throw new IllegalArgumentException("Organization name is required");
            }
            if (request.registrationNumber() == null || request.registrationNumber().trim().isEmpty()) {
                throw new IllegalArgumentException("Registration number is required for organizations");
            }
            
            roles = Set.of(org.example.charityapp.entities.UserRole.ROLE_ORGANIZATION);
        } else {
            // Default to USER role
            roles = Set.of(org.example.charityapp.entities.UserRole.ROLE_USER);
        }

        // Create and save user
        User user = new User(
                request.username(),
                request.email(),
                roles,
                passwordEncoder.encode(request.password())
        );
        user = userRepository.save(user);

        // If this is an organization registration, create the organization
        if ("ROLE_ORGANIZATION".equals(request.role())) {
            Organization organization = new Organization(request.organizationName());
            organization.setRegistrationNumber(request.registrationNumber());
            organization.setDescription(request.description());
            organization.setStatus("PENDING");
            organization.setUser(user); // Link the organization to the user
            organizationService.createOrganization(organization);
            
            return "Organization registration submitted for approval";
        }

        return "User registered successfully";
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        // Check if user is enabled
        if (!user.isEnabled()) {
            throw new IllegalArgumentException("Account is disabled");
        }

        // Generate JWT token
        return jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role.name()))
                        .toList()
        ));
    }
}