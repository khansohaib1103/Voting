package com.voting.system.candidateservice.repository;


import com.voting.system.candidateservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUserCNIC(String CNIC);

    List<User> findByUserConstituency(String findByUserConstituency);
    boolean existsByUserCNIC(String userCNIC);

    Optional<User> findByUserName(String userName);
    boolean existsByUserName(String userName);

    Optional<User> findUsernameByUserCNIC(String userCNIC);
}
