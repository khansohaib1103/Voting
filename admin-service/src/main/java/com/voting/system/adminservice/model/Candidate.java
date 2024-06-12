package com.voting.system.adminservice.model;

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
@Table(name = "candidate_info")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long candidateID;
    String candidatePartyName;

    @Lob
    @Column(name = "partySymbolImage")
    byte[] candidateSymbolImage;

    @OneToOne
    private User user;
}
