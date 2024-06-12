package com.voting.system.votingservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "candidate_info")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long candidateID;

    private String candidatePartyName;

    @Lob
    @Column(name = "partySymbolImage")
    private byte[] candidateSymbolImage;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
    private List<Voting> receivedVotes;
}
