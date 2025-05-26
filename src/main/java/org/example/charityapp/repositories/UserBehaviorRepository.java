package org.example.charityapp.repositories;

import org.example.charityapp.entities.UserBehavior;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * MongoDB repository for storing and retrieving user behavior events.
 */
@Repository
public interface UserBehaviorRepository extends JpaRepository<UserBehavior, Long> {
    List<UserBehavior> findByUser_Id(Long userId);
    List<UserBehavior> findByActionType(String actionType);
    List<UserBehavior> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    List<UserBehavior> findByUser_IdAndActionType(Long userId, String actionType);
}
