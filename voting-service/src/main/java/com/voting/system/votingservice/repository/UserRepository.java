package com.voting.system.votingservice.repository;

import com.voting.system.votingservice.model.Candidate;
import com.voting.system.votingservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserCNIC(String userCNIC);
    boolean existsByUserCNIC(String userCNIC);

    Optional<User> findByUserName(String userName);

    Optional<User> findUsernameByUserCNIC(String userCNIC);
    boolean existsByUserName(String userName);



}
