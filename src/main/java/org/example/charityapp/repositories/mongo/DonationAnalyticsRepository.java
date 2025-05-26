package org.example.charityapp.repositories.mongo;

import org.example.charityapp.entities.mongo.DonationAnalytics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface DonationAnalyticsRepository extends MongoRepository<DonationAnalytics, String> {
    
    List<DonationAnalytics> findByCharityActionId(Long charityActionId);
    
    List<DonationAnalytics> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("{'amount': {$gt: ?0}}")
    List<DonationAnalytics> findByAmountGreaterThan(Double amount);
    
    List<DonationAnalytics> findByStatus(String status);
    
    @Query(value = "{'charityActionId': ?0}", count = true)
    long countByCharityActionId(Long charityActionId);
} 