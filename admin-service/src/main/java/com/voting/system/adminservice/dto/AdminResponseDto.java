package com.voting.system.adminservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminResponseDto {

    long userID;
    String userCNIC;
    String userName;
    String userConstituency;
    byte[] userImage;
}