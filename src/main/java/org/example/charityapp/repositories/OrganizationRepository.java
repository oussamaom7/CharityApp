package org.example.charityapp.repositories;

import org.example.charityapp.entities.Organization;
import org.example.charityapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    List<Organization> findByStatus(String status);
    
    Optional<Organization> findByUser(User user);
    
    boolean existsByContactEmail(String email);
    
    @Query("SELECT o FROM Organization o WHERE LOWER(o.name) LIKE LOWER(concat('%', :query, '%')) " +
           "OR LOWER(o.description) LIKE LOWER(concat('%', :query, '%'))")
    List<Organization> search(@Param("query") String query);
    
    @Query("SELECT o FROM Organization o WHERE o.status = 'APPROVED' ORDER BY o.registrationDate DESC")
    List<Organization> findLatestApproved(int limit);
    
    long countByStatus(String status);
}