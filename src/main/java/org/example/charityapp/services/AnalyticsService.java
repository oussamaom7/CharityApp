package org.example.charityapp.services;

import org.example.charityapp.entities.mongo.DonationAnalytics;
import org.example.charityapp.repositories.mongo.DonationAnalyticsRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnalyticsService {
    
    private final DonationAnalyticsRepository analyticsRepository;
    
    public AnalyticsService(DonationAnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }
    
    public void saveDonationAnalytics(DonationAnalytics analytics) {
        analyticsRepository.save(analytics);
    }
    
    public List<DonationAnalytics> getDonationsByCharityAction(Long charityActionId) {
        return analyticsRepository.findByCharityActionId(charityActionId);
    }
    
    public List<DonationAnalytics> getDonationsByDateRange(LocalDateTime start, LocalDateTime end) {
        return analyticsRepository.findByTimestampBetween(start, end);
    }
    
    public List<DonationAnalytics> getLargeDonations(Double threshold) {
        return analyticsRepository.findByAmountGreaterThan(threshold);
    }
    
    public long getDonationCountForCharityAction(Long charityActionId) {
        return analyticsRepository.countByCharityActionId(charityActionId);
    }
    
    public List<DonationAnalytics> getDonationsByStatus(String status) {
        return analyticsRepository.findByStatus(status);
    }
} 