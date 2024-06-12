package com.voting.system.userservice.repository;

import com.voting.system.userservice.model.Candidate;
import com.voting.system.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    Optional<Candidate> findByUser(User user);

    List<Candidate> findByUser_UserConstituency(String userCNIC);
}
