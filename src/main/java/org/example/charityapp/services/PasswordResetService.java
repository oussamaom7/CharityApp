package org.example.charityapp.services;

import org.example.charityapp.entities.PasswordResetToken;
import org.example.charityapp.entities.User;
import org.example.charityapp.repositories.PasswordResetTokenRepository;
import org.example.charityapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {
    private static final int EXPIRATION_HOURS = 1;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;

    public String createPasswordResetToken(User user) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusHours(EXPIRATION_HOURS);
        tokenRepository.deleteByUser(user); // Only one token per user
        PasswordResetToken resetToken = new PasswordResetToken(token, user, expiry);
        tokenRepository.save(resetToken);
        return token;
    }

    public Optional<User> validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> resetTokenOpt = tokenRepository.findByToken(token);
        if (resetTokenOpt.isPresent()) {
            PasswordResetToken resetToken = resetTokenOpt.get();
            if (resetToken.getExpiryDate().isAfter(LocalDateTime.now())) {
                return Optional.of(resetToken.getUser());
            }
        }
        return Optional.empty();
    }

    public boolean updatePassword(User user, String newPassword) {
        userRepository.findById(user.getId()).ifPresent(u -> {
            // You may want to encode the password here using a PasswordEncoder
            u.setPassword(newPassword);
            userRepository.save(u);
        });
        tokenRepository.deleteByUser(user);
        return true;
    }
}
