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
@Table(name = "polling_info")
public class Polling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pollingId;

    LocalDateTime startDateTime;

    LocalDateTime endDateTime;

    @OneToMany(mappedBy = "polling", cascade = CascadeType.ALL)
    private List<Voting> votings;
}
