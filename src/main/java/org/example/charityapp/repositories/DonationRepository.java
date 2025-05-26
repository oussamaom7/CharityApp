package org.example.charityapp.repositories;

import org.example.charityapp.entities.Donation;
import org.example.charityapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByStatus(String status);
    List<Donation> findByUser(User user);
    List<Donation> findTop5ByOrderByIdDesc();
}