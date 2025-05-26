package org.example.charityapp.repositories;

import org.example.charityapp.entities.Volunteer;
import org.example.charityapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    List<Volunteer> findByUser(User user);
    List<Volunteer> findTop5ByOrderByIdDesc();
}
