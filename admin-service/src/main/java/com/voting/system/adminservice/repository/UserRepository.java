package com.voting.system.adminservice.repository;

import com.voting.system.adminservice.model.Role;
import com.voting.system.adminservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserCNIC(String CNIC);

    Optional<User> findByUserName(String userName);

    int countUsersByRoleRoleName(String roleName);
    boolean existsByUserName(String userName);

    boolean existsByRoleRoleName(String roleName);

    boolean existsByUserCNIC(String userCNIC);

}
