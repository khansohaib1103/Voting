package com.voting.system.adminservice.repository;

import com.voting.system.adminservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(String roleName);

    boolean existsByRoleName(String Name);
}
