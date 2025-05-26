package org.example.charityapp.services;

import org.example.charityapp.entities.UserActionLog;
import org.example.charityapp.repositories.UserActionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserActionLogService {
    @Autowired
    private UserActionLogRepository logRepository;

    public void logAction(Long userId, String username, String action, String details) {
        UserActionLog log = new UserActionLog(userId, username, action, details, LocalDateTime.now());
        logRepository.save(log);
    }

    public List<UserActionLog> getRecentLogs(int limit) {
        return logRepository.findRecentLogs(PageRequest.of(0, limit));
    }
}
