package com.voting.system.candidateservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CandidateResponseDto {
    long candidateID;
    String candidatePartyName;
    byte[] candidateSymbolImage;
}
