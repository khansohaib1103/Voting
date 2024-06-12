package com.voting.system.userservice.feign;

import com.voting.system.userservice.model.Candidate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public interface CandidateUserClient {

    @GetMapping("/getAllCandidates")
    @ResponseStatus(HttpStatus.OK)
    List<Candidate> getAllUsers();
}
