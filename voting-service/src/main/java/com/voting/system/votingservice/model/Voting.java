package com.voting.system.votingservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "voting_info")
public class Voting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long voteId;

    @ManyToOne
    @JoinColumn(name = "voter_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "polling_id")
    private Polling polling;
}
