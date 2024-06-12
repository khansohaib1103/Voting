package com.voting.system.userservice.repository;

import com.voting.system.userservice.dto.UserRequestDto;
import com.voting.system.userservice.dto.UserResponseDto;
import com.voting.system.userservice.model.Candidate;
import com.voting.system.userservice.model.Role;
import com.voting.system.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByUserCNIC(String userCNIC);
    boolean existsByUserCNIC(String userCNIC);

    Optional<User> findByUserName(String userName);
    boolean existsByUserName(String userName);
}
