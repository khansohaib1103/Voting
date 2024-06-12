package com.voting.system.candidateservice.feign;

import com.voting.system.candidateservice.model.Candidate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@FeignClient("ADMIN-SERVICE")
public interface CandidateAdminClient {
    @PostMapping("/api/approveCandidate/{userCNIC}")
    Boolean validateCandidate(@PathVariable("userCNIC") String userCNIC);

}
