package com.voting.system.candidateservice.repository;

import com.voting.system.candidateservice.model.Candidate;
import com.voting.system.candidateservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    Optional<Candidate> findByUser(User user);

    List<Candidate> findByUser_UserConstituencyAndCandidatePartyName(String constituency,
                                                                     String partyName);
}
