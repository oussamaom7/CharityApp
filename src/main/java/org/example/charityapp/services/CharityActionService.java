package org.example.charityapp.services;

import org.example.charityapp.DTO.CharityActionRequest;
import org.example.charityapp.DTO.CharityActionResponse;
import org.example.charityapp.entities.CharityAction;
import org.example.charityapp.entities.Organization;
import org.example.charityapp.entities.User;
import org.example.charityapp.repositories.CharityActionRepository;
import org.example.charityapp.repositories.OrganizationRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CharityActionService {

    private final CharityActionRepository charityActionRepository;
    private final OrganizationRepository organizationRepository;

    public CharityActionService(CharityActionRepository charityActionRepository, OrganizationRepository organizationRepository) {
        this.charityActionRepository = charityActionRepository;
        this.organizationRepository = organizationRepository;
    }

    public List<CharityActionResponse> getAllCharityActions() {
        return charityActionRepository.findAll().stream()
                .map(action -> new CharityActionResponse(
                        action.getId(),
                        action.getTitle(),
                        action.getDescription(),
                        action.getOrganization().getId(),
                        action.getOrganization().getName(),
                        action.getGoalAmount(),
                        action.getCurrentAmount(),
                        action.getStatus()))
                .collect(Collectors.toList());
    }

    public CharityActionResponse getCharityActionById(Long id) {
        CharityAction action = charityActionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Charity action not found"));
        return new CharityActionResponse(
                action.getId(),
                action.getTitle(),
                action.getDescription(),
                action.getOrganization().getId(),
                action.getOrganization().getName(),
                action.getGoalAmount(),
                action.getCurrentAmount(),
                action.getStatus());
    }

    public CharityAction createCharityActionEntity(CharityActionRequest request) {
        Organization org = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new IllegalArgumentException("Organization not found"));
        CharityAction action = new CharityAction(request.title(), request.description(), org);
        // Set status to PENDING by default and set the goal amount
        action.setStatus("PENDING");
        action.setGoalAmount(request.goalAmount());
        action.setCurrentAmount(0.0); // Initialize current amount to 0
        return charityActionRepository.save(action);
    }

    public CharityActionResponse createCharityAction(CharityActionRequest request) {
        CharityAction action = createCharityActionEntity(request);
        return new CharityActionResponse(
                action.getId(),
                action.getTitle(),
                action.getDescription(),
                action.getOrganization().getId(),
                action.getOrganization().getName(),
                action.getGoalAmount(),
                action.getCurrentAmount(),
                action.getStatus());
    }

    public void deleteCharityAction(Long id) {
        CharityAction action = charityActionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Charity action not found"));
        charityActionRepository.delete(action);
    }

    public List<CharityAction> getPendingCharityActions() {
        return charityActionRepository.findByStatus("PENDING");
    }

    public List<CharityAction> getCharityActionsByOrganization(Organization organization) {
        return charityActionRepository.findByOrganization(organization);
    }
    
    public List<CharityActionResponse> getApprovedCharityActions() {
        return charityActionRepository.findByStatus("APPROVED").stream()
                .map(action -> new CharityActionResponse(
                        action.getId(),
                        action.getTitle(),
                        action.getDescription(),
                        action.getOrganization().getId(),
                        action.getOrganization().getName(),
                        action.getGoalAmount(),
                        action.getCurrentAmount(),
                        action.getStatus()
                ))
                .collect(Collectors.toList());
    }

    public List<CharityActionResponse> getCharityActionsForApprovedOrganizations() {
        return charityActionRepository.findAll().stream()
                .filter(action ->
                        action.getOrganization() != null &&
                        "APPROVED".equals(action.getOrganization().getStatus())
                )
                .map(action -> new CharityActionResponse(
                        action.getId(),
                        action.getTitle(),
                        action.getDescription(),
                        action.getOrganization().getId(),
                        action.getOrganization().getName(),
                        action.getGoalAmount(),
                        action.getCurrentAmount(),
                        action.getStatus()))
                .collect(Collectors.toList());
    }

    public void approveCharityAction(Long id) {
        var action = charityActionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Charity Action not found"));
        action.setStatus("APPROVED");
        charityActionRepository.save(action);
    }

    public void rejectCharityAction(Long id) {
        var action = charityActionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Charity Action not found"));
        action.setStatus("REJECTED");
        charityActionRepository.save(action);
    }
}