package org.example.charityapp.repositories;

import org.example.charityapp.entities.UserActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface UserActionLogRepository extends JpaRepository<UserActionLog, Long> {
    List<UserActionLog> findTop20ByOrderByTimestampDesc();

    @Query("SELECT u FROM UserActionLog u ORDER BY u.timestamp DESC")
    List<UserActionLog> findRecentLogs(Pageable pageable);
}
