package com.voting.system.adminservice.repository;

import com.voting.system.adminservice.model.Candidate;
import com.voting.system.adminservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    Optional<Candidate> findByUser(User user);
}
