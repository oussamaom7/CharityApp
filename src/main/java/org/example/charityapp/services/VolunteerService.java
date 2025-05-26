package org.example.charityapp.services;

import org.example.charityapp.entities.Volunteer;
import org.example.charityapp.entities.User;
import org.example.charityapp.repositories.VolunteerRepository;
import org.example.charityapp.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VolunteerService {
    private final VolunteerRepository volunteerRepository;
    private final UserRepository userRepository;

    public VolunteerService(VolunteerRepository volunteerRepository, UserRepository userRepository) {
        this.volunteerRepository = volunteerRepository;
        this.userRepository = userRepository;
    }

    public void saveVolunteer(String name, String email, String interest, String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        Volunteer volunteer = new Volunteer(name, email, interest, user);
        volunteerRepository.save(volunteer);
    }

    public List<Volunteer> getAllVolunteers() {
        return volunteerRepository.findAll();
    }

    public List<Volunteer> getVolunteersByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return List.of();
        return volunteerRepository.findByUser(user);
    }

    public long getTotalVolunteersCount() {
        return volunteerRepository.count();
    }

    public List<Volunteer> getRecentVolunteers(int n) {
        // Always use 5 for now since repository method is fixed
        return volunteerRepository.findTop5ByOrderByIdDesc();
    }
}
