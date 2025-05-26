package org.example.charityapp.services;

import org.example.charityapp.entities.Organization;
import org.example.charityapp.entities.User;
import org.example.charityapp.repositories.OrganizationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public Organization getOrganizationById(Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Organization not found"));
    }

    // This method is kept for backward compatibility
    public void createOrganization(Organization organization) {
        saveOrganization(organization);
    }

    public void deleteOrganization(Long id) {
        if (!organizationRepository.existsById(id)) {
            throw new IllegalArgumentException("Organization not found");
        }
        organizationRepository.deleteById(id);
    }

    public List<Organization> getPendingOrganizations() {
        return organizationRepository.findByStatus("PENDING");
    }

    public List<Organization> getApprovedOrganizations() {
        return organizationRepository.findByStatus("APPROVED");
    }

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }
    
    public Optional<Organization> findByUser(User user) {
        return organizationRepository.findByUser(user);
    }
    
    public Organization findByUserOrThrow(User user) {
        return organizationRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Organization not found for user"));
    }
    
    public Organization approveOrganization(Long id) {
        Organization org = getOrganizationById(id);
        org.setStatus("APPROVED");
        return organizationRepository.save(org);
    }
    
    public void rejectOrganization(Long id) {
        Organization org = getOrganizationById(id);
        // Send rejection email before deletion
        // emailService.sendRejectionEmail(org.getContactEmail(), org.getName());
        organizationRepository.delete(org);
    }
    
    public boolean existsByEmail(String email) {
        return organizationRepository.existsByContactEmail(email);
    }
    
    public Organization saveOrganization(Organization organization) {
        // Additional validation or business logic can be added here
        if (organization.getId() == null && existsByEmail(organization.getContactEmail())) {
            throw new IllegalArgumentException("An organization with this email already exists");
        }
        if (organization.getId() == null) {
            organization.setStatus("PENDING"); // Default status for new organizations
        }
        return organizationRepository.save(organization);
    }
}