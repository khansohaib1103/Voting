package com.voting.system.candidateservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voting.system.candidateservice.dto.*;
import com.voting.system.candidateservice.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class candidateController {

    @Autowired
    private final CandidateService candidateService;

    @PostMapping("/saveCandidate")
    public ResponseEntity<String> saveCandidate(@RequestParam("candidateRequestDto") String candidateRequestDtoJson,
                                                @RequestParam("userCNIC") String userCNIC,
                                                @RequestParam("file") MultipartFile file) throws JsonProcessingException {
        CandidateRequestDto candidateRequestDto = new ObjectMapper().readValue(candidateRequestDtoJson, CandidateRequestDto.class);

        return candidateService.createCandidate(candidateRequestDto, userCNIC, file);
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam("loginDto") String  loginDtoJSON) throws JsonProcessingException{
        LoginDto loginDto = new ObjectMapper().readValue(loginDtoJSON, LoginDto.class);
        boolean userAuthenticated = candidateService.loginUser(loginDto);
        if (userAuthenticated) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User Logged in successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(" " + HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllUsers")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDto> getAllUsers() {
        return candidateService.getAllUsers();
    }

    @GetMapping(value = "/getUserByCNIC/{userCNIC}")
    public List<UserResponseDto> getUserByCNIC(@PathVariable("userCNIC") String CNIC){
        return candidateService.getUserByCNIC(CNIC);
    }

    @GetMapping("/getAllCandidates")
    @ResponseStatus(HttpStatus.OK)
    List<CandidateResponseDto> getAllCandidates(){
        return candidateService.getAllCandidates();
    }

    @GetMapping("/getAllUsers/{userCNIC}")
    @ResponseStatus(HttpStatus.OK)
    List<UserResponseDto> getAllUsersByConstituency(@PathVariable("userCNIC")String userCNIC){
        return candidateService.getAllUsersByConstituency(userCNIC);
    }

}
