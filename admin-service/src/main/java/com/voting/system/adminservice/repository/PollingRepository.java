package com.voting.system.adminservice.repository;

import com.voting.system.adminservice.model.Polling;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PollingRepository extends JpaRepository<Polling, Long> {


}
