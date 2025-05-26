package org.example.charityapp.services;

import org.example.charityapp.DTO.DonationRequest;
import org.example.charityapp.DTO.DonationResponse;
import org.example.charityapp.entities.CharityAction;
import org.example.charityapp.entities.Donation;
import org.example.charityapp.entities.User;
import org.example.charityapp.repositories.CharityActionRepository;
import org.example.charityapp.repositories.DonationRepository;
import org.example.charityapp.repositories.UserRepository;
import org.example.charityapp.services.PaymentGateway;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonationService {

    private final DonationRepository donationRepository;
    private final CharityActionRepository charityActionRepository;
    private final UserRepository userRepository;
    private final PaymentGateway paymentGateway;

    public DonationService(DonationRepository donationRepository, CharityActionRepository charityActionRepository, UserRepository userRepository, PaymentGateway paymentGateway) {
        this.donationRepository = donationRepository;
        this.charityActionRepository = charityActionRepository;
        this.userRepository = userRepository;
        this.paymentGateway = paymentGateway;
    }

    public DonationResponse createDonation(DonationRequest request) {
        if (request.getAmount().compareTo(new BigDecimal("0.01")) < 0) {
            throw new IllegalArgumentException("Amount must be at least 0.01");
        }
        CharityAction action = charityActionRepository.findByTitle(request.getCharityActionName())
                .orElseThrow(() -> new IllegalArgumentException("Charity action not found by name: " + request.getCharityActionName()));
        Donation donation = new Donation(request.getAmount(), request.getDonorName(), action);
        // Set status to PENDING by default
        donation.setStatus("PENDING");
        // Associate donation with current user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String username = auth.getName();
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                donation.setUser(user);
            }
        }
        // Process payment before saving donation
        try {
            paymentGateway.processPayment(donation);
            donation.setStatus("APPROVED");
        } catch (Exception e) {
            donation.setStatus("FAILED");
        }
        donation = donationRepository.save(donation);
        return new DonationResponse(
                donation.getId(),
                donation.getAmount(),
                donation.getDonorName(),
                donation.getCharityAction() != null ? donation.getCharityAction().getTitle() : "-",
                donation.getDate(),
                donation.getStatus()
        );
    }

    public List<DonationResponse> getAllDonations() {
        return donationRepository.findAll().stream()
                .map(donation -> new DonationResponse(
                        donation.getId(),
                        donation.getAmount(),
                        donation.getDonorName(),
                        donation.getCharityAction() != null ? donation.getCharityAction().getTitle() : "-",
                        donation.getDate(),
                        donation.getStatus()))
                .collect(Collectors.toList());
    }

    public List<Donation> getPendingDonations() {
        return donationRepository.findByStatus("PENDING");
    }

    public List<DonationResponse> getUserDonations(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return List.of();
        return donationRepository.findByUser(user).stream()
                .map(donation -> new DonationResponse(
                        donation.getId(),
                        donation.getAmount(),
                        donation.getDonorName(),
                        donation.getCharityAction() != null ? donation.getCharityAction().getTitle() : "-",
                        donation.getDate(),
                        donation.getStatus()))
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalDonationsAmount() {
        return donationRepository.findAll().stream()
                .map(Donation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<DonationResponse> getRecentDonations(int n) {
        // Always use 5 for now since repository method is fixed
        return donationRepository.findTop5ByOrderByIdDesc().stream()
                .map(donation -> new DonationResponse(
                        donation.getId(),
                        donation.getAmount(),
                        donation.getDonorName(),
                        donation.getCharityAction() != null ? donation.getCharityAction().getTitle() : "-",
                        donation.getDate(),
                        donation.getStatus()))
                .collect(Collectors.toList());
    }

    public void approveDonation(Long id) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Donation not found"));
        donation.setStatus("APPROVED");
        
        // Update the charity action's current amount
        CharityAction action = donation.getCharityAction();
        if (action != null) {
            Double currentAmount = action.getCurrentAmount() != null ? action.getCurrentAmount() : 0.0;
            Double donationAmount = donation.getAmount().doubleValue();
            action.setCurrentAmount(currentAmount + donationAmount);
            charityActionRepository.save(action);
        }
        
        donationRepository.save(donation);
    }

    public void rejectDonation(Long id) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Donation not found"));
        donation.setStatus("REJECTED");
        donationRepository.save(donation);
    }

    // For admin add-donation form
    public List<CharityAction> getAllCharityActions() {
        // Implement this method to return all charity actions for the dropdown
        return charityActionRepository.findAll();
    }

    public void createDonationFromAdmin(BigDecimal amount, String donorName, Long charityActionId) {
        CharityAction action = charityActionRepository.findById(charityActionId).orElseThrow();
        Donation donation = new Donation(amount, donorName, action);
        donation.setStatus("PENDING");
        donation.setDate(java.time.LocalDateTime.now());
        donationRepository.save(donation);
    }

    public DonationResponse getDonationById(Long id) {
        return donationRepository.findById(id)
            .map(donation -> new DonationResponse(
                donation.getId(),
                donation.getAmount(),
                donation.getDonorName(),
                donation.getCharityAction() != null ? donation.getCharityAction().getTitle() : "-",
                donation.getDate(),
                donation.getStatus()
            ))
            .orElseThrow(() -> new IllegalArgumentException("Donation not found with id: " + id));
    }

    /**
     * Returns the Donation entity by id (helper for payment flow).
     */
    public Donation findDonationEntityById(Long id) {
        return donationRepository.findById(id).orElse(null);
    }

    /**
     * Returns the PaymentGateway instance (helper for payment flow).
     */
    public PaymentGateway getPaymentGateway() {
        return paymentGateway;
    }
}