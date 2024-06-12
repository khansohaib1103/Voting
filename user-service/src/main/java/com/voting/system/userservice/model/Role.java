package com.voting.system.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "role_info")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long roleId;

    @Column(nullable = false, unique = true)
    private String roleName;
}
