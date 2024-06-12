package com.voting.system.adminservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

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
}
