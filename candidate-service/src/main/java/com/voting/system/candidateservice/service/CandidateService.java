package com.voting.system.candidateservice.service;

import com.voting.system.candidateservice.dto.CandidateRequestDto;
import com.voting.system.candidateservice.dto.CandidateResponseDto;
import com.voting.system.candidateservice.dto.LoginDto;
import com.voting.system.candidateservice.dto.UserResponseDto;
import com.voting.system.candidateservice.feign.CandidateAdminClient;
import com.voting.system.candidateservice.feign.CandidateUserClient;
import com.voting.system.candidateservice.model.Candidate;
import com.voting.system.candidateservice.model.User;
import com.voting.system.candidateservice.repository.CandidateRepository;
import com.voting.system.candidateservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandidateService {

    @Autowired
    private CandidateUserClient candidateUserClient;

    @Autowired
    private CandidateAdminClient candidateAdminClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    public ResponseEntity<String> createCandidate(CandidateRequestDto candidateRequestDto, String CNIC, MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        Optional<User> userOptional = userRepository.findUsernameByUserCNIC(CNIC);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            String loggedInUserCNIC = getUserCNICFromAuthentication(authentication);
            String userName = user.getUserName();
            if (!userName.equals(loggedInUserCNIC)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User with CNIC " + CNIC + " is not logged in");
            }
        }

        if (!candidateAdminClient.validateCandidate(CNIC)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Candidate with CNIC " + CNIC + " already exists");
        }

        List<User> usersByCNIC = candidateUserClient.getUserByCNIC(CNIC);
        if (usersByCNIC.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with CNIC " + CNIC + " not found, first register as a voter");
        }

        User user = usersByCNIC.get(0);
        List<Candidate> candidatesWithSamePartyName = candidateRepository.findByUser_UserConstituencyAndCandidatePartyName(user.getUserConstituency(), candidateRequestDto.getCandidatePartyName());
        if (!candidatesWithSamePartyName.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A candidate with party name " + candidateRequestDto.getCandidatePartyName() + " already exists in constituency " + user.getUserConstituency());
        }

        try {
            Candidate candidate = Candidate.builder()
                    .candidatePartyName(candidateRequestDto.getCandidatePartyName())
                    .user(user)
                    .build();
            candidate.setCandidateSymbolImage(file.getBytes());
            candidateRepository.save(candidate);
            return ResponseEntity.status(HttpStatus.CREATED).body("Candidate saved successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image for candidate " + user.getUserCNIC());
        }
    }

    private String getUserCNICFromAuthentication(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }


    public boolean loginUser(LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUserName(),
                        loginDto.getUserPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return true;
    }

    public List<UserResponseDto> getAllUsersByConstituency(String userCNIC){
        try {
            List<User> users = userRepository.findByUserCNIC(userCNIC);
            if(!users.isEmpty()){
                User user = users.get(0);
                Optional<Candidate> existingCandidate = candidateRepository.findByUser(user);
                if (existingCandidate.isPresent()){
                    String candidateConstituency = existingCandidate.get().getUser().getUserConstituency();

                    List<User> usersWithSameConstituency = userRepository.findByUserConstituency(candidateConstituency);

                    return usersWithSameConstituency.stream()
                            .map(this::mapToUserResponse)
                            .collect(Collectors.toList());
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<UserResponseDto> getAllUsers() {
        List<User> users = candidateUserClient.getAllUsers();

        return users.stream().map(this::mapToUserResponse).toList();
    }

    public List<UserResponseDto> getUserByCNIC(String CNIC){
        List<User> usersByCNIC = candidateUserClient.getUserByCNIC(CNIC);
        return usersByCNIC.stream()
                .map(this::mapToUserResponse)
                .toList();
    }


    private UserResponseDto mapToUserResponse(User user) {
        return UserResponseDto.builder()
                .userID(user.getUserID())
                .userName(user.getUserName())
                .userCNIC(user.getUserCNIC())
                .userConstituency(user.getUserConstituency())
                .userImage(user.getUserImage())
                .build();
    }

    public List<CandidateResponseDto> getAllCandidates() {
        List<Candidate> candidates = candidateRepository.findAll();

        return candidates.stream().map(this::mapToCandidateResponse).toList();
    }

    private CandidateResponseDto mapToCandidateResponse(Candidate candidate) {
        return CandidateResponseDto.builder()
                .candidateID(candidate.getCandidateID())
                .candidateSymbolImage(candidate.getCandidateSymbolImage())
                .candidatePartyName(candidate.getCandidatePartyName())
                .build();
    }


}
