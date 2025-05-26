package org.example.charityapp.services;

import org.example.charityapp.entities.User;
import java.util.List;

public interface UserServiceInterface {
    String getUserDetails(Long id);
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    List<User> getAllUsers();
    long getTotalUsersCount();
    void promoteToAdmin(Long userId);
    void demoteToUser(Long userId);
    boolean existsByEmail(String email);
    User save(User user);
    void createUser(User user);
    User registerUser(String username, String email, String password, String role);
}
