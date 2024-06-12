package com.voting.system.candidateservice.feign;

import com.voting.system.candidateservice.dto.UserResponseDto;
import com.voting.system.candidateservice.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@FeignClient("USER-SERVICE")
public interface CandidateUserClient {

    @GetMapping("/api/getAllUsers")
    @ResponseStatus(HttpStatus.OK)
     List<User> getAllUsers();

    @GetMapping(value = "/api/getUserByCNIC/{userCNIC}")
     List<User> getUserByCNIC(@PathVariable("userCNIC") String CNIC);

}
