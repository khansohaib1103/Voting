package com.voting.system.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Lob;

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
