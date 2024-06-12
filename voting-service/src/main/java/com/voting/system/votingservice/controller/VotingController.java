package com.voting.system.votingservice.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voting.system.votingservice.model.LoginDto;
import com.voting.system.votingservice.service.VotingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class VotingController {

    @Autowired
    private VotingService votingService;


    @PostMapping("/vote")
    public ResponseEntity<String> postVote(@RequestParam("userCNIC") String userCNIC,
                         @RequestParam("candidateCNIC") String candidateCNIC) {
        return votingService.castVote(userCNIC, candidateCNIC);
    }
    @GetMapping("/winner")
    public ResponseEntity<String> winner(@RequestParam("userCNIC")String userCNIC,
                       @RequestParam("pollingId") long pollingId){
        return votingService.calculateWinner(userCNIC, pollingId);
    }

    @GetMapping("/allWinners")
    public ResponseEntity<String> WinnerInAllConstituency(@RequestParam("pollingId") long pollingId){
        return votingService.calculateWinnerInAllConstituency(pollingId);
    }

    @GetMapping("/checkVotes")
    public ResponseEntity<String> checkVotes(@RequestParam("candidateCNIC")String userCNIC,
                       @RequestParam("pollingId") long pollingId){
        return votingService.checkVotes(userCNIC, pollingId);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam("loginDto") String  loginDtoJSON) throws JsonProcessingException{
        LoginDto loginDto = new ObjectMapper().readValue(loginDtoJSON, LoginDto.class);
        return votingService.loginUser(loginDto);
    }
}
