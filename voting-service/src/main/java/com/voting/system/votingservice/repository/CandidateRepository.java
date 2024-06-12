package com.voting.system.votingservice.repository;

import com.voting.system.votingservice.model.Candidate;
import com.voting.system.votingservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    Optional<Candidate> findByUser_UserCNIC(String candidateCNIC);



}
