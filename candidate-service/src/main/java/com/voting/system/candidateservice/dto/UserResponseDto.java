package com.voting.system.candidateservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    long userID;
    String userCNIC;
    String userName;
    String userConstituency;
    byte[] userImage;
    String userPassword;
}
