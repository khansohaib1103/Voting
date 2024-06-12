package com.voting.system.votingservice.repository;

import com.voting.system.votingservice.model.Candidate;
import com.voting.system.votingservice.model.Polling;
import com.voting.system.votingservice.model.User;
import com.voting.system.votingservice.model.Voting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VotingRepository extends JpaRepository<Voting, Long> {

    List<Voting> findByUser_UserCNIC(String userCNIC);

    List<Voting> findByUser_UserCNICAndPolling(String userCNIC, Polling polling);
    List<Voting> findByUserAndPolling(User user, Polling polling);

    List<Voting> findByPollingAndUser_UserCNIC(Polling polling, String userCNIC);
}
