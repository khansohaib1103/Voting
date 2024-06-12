package com.voting.system.votingservice.repository;

import com.voting.system.votingservice.model.Polling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PollingRepository extends JpaRepository<Polling, Long> {

   /* @Query("SELECT p FROM Polling p WHERE p.startDateAndTime <= ?1 ORDER BY p.startDateAndTime DESC")
    List<Polling> findByStartDateBeforeOrEqualOrderByStartDateDesc(LocalDateTime currentDateTime);

    @Query("SELECT p FROM Polling p WHERE p.endDateAndTime >= ?1 ORDER BY p.endDateAndTime ASC")
    List<Polling> findByEndDateAfterOrEqualOrderByEndDateAsc(LocalDateTime currentDateTime);*/

    List<Polling> findByStartDateTimeBeforeAndEndDateTimeAfter(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
