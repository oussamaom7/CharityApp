package org.example.charityapp.services;

import org.example.charityapp.entities.User;
import org.example.charityapp.entities.UserBehavior;
import org.example.charityapp.repositories.UserBehaviorRepository;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class UserBehaviorService {
    
    private final UserBehaviorRepository userBehaviorRepository;
    
    public UserBehaviorService(UserBehaviorRepository userBehaviorRepository) {
        this.userBehaviorRepository = userBehaviorRepository;
    }
    
    public void trackUserAction(User user, String actionType, String actionDetails, HttpServletRequest request) {
        UserBehavior behavior = new UserBehavior(
            user,
            actionType,
            actionDetails,
            LocalDateTime.now(),
            request.getRemoteAddr(),
            request.getHeader("User-Agent")
        );
        
        userBehaviorRepository.save(behavior);
    }
    
    public void trackAnonymousAction(String actionType, String actionDetails, HttpServletRequest request) {
        UserBehavior behavior = new UserBehavior(
            null,
            actionType,
            actionDetails,
            LocalDateTime.now(),
            request.getRemoteAddr(),
            request.getHeader("User-Agent")
        );
        
        userBehaviorRepository.save(behavior);
    }
} 