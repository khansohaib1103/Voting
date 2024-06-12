package com.voting.system.userservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    String userCNIC;
    String userName;
    String userConstituency;
    byte[] userImage;
    String userPassword;
    String Role;
}
