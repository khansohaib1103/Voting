package com.voting.system.votingservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_info")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userID;

    private String userCNIC;
    private String userName;
    private String userConstituency;
    private String userPassword;

    @Lob
    @Column(name = "userImage")
    private byte[] userImage;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Candidate candidate;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "userID"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "roleId"))
    private List<Role> role = new ArrayList<>();
}
