package org.example.charityapp.repositories;

import org.example.charityapp.entities.CharityAction;
import org.example.charityapp.entities.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CharityActionRepository extends JpaRepository<CharityAction, Long> {
    List<CharityAction> findByStatus(String status);
    List<CharityAction> findByOrganization(Organization organization);
    List<CharityAction> findByOrganizationAndStatus(Organization organization, String status);
    Optional<CharityAction> findByTitle(String title);
    
    default List<CharityAction> findPendingActions() {
        return findByStatus("PENDING");
    }
    
    default List<CharityAction> findApprovedActions() {
        return findByStatus("APPROVED");
    }
}