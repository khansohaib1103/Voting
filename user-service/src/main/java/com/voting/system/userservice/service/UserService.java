package com.voting.system.userservice.service;

import com.voting.system.userservice.dto.LoginDto;
import com.voting.system.userservice.dto.UserRequestDto;
import com.voting.system.userservice.dto.UserResponseDto;
import com.voting.system.userservice.feign.UserAdminClient;
import com.voting.system.userservice.model.Candidate;
import com.voting.system.userservice.model.Role;
import com.voting.system.userservice.model.User;
import com.voting.system.userservice.repository.CandidateRepository;
import com.voting.system.userservice.repository.RoleRepository;
import com.voting.system.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserAdminClient userAdminClient;
    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;


    public ResponseEntity<String> createUser(UserRequestDto userRequestDto, MultipartFile file) {

        if(!userRepository.existsByUserName(userRequestDto.getUserName())) {
            String userCNIC = userRequestDto.getUserCNIC();
            boolean check = userAdminClient.validateUser(userCNIC);

             if (check) {
            try {
                String encodedPassword = passwordEncoder.encode(userRequestDto.getUserPassword()); // Encode password
                User user = User.builder()
                        .userCNIC(userCNIC)
                        .userName(userRequestDto.getUserName())
                        .userConstituency(userRequestDto.getUserConstituency())
                        .userImage(file.getBytes())
                        .userPassword(encodedPassword)
                        .build();

                Role roles = roleRepository.findByRoleName("USER").get();
                if(roles != null) {
                    user.setRole(Collections.singletonList(roles));
                    userRepository.save(user);
                    return ResponseEntity.status(HttpStatus.OK).body("User " + userCNIC + " saved successfully...");
                }else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Role 'USER' not found");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User " + userCNIC + " already exists in database");
            }
             }
          else {
                 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User " + userCNIC + " already exists in database");
             }
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User name already taken.");
        }
    }

    public ResponseEntity<String> loginUser(LoginDto loginDto) {
        String username = loginDto.getUserName();
        Optional<User> user = userRepository.findByUserName(username);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(username + " doesn't exists");
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUserName(),
                            loginDto.getUserPassword()));

            return ResponseEntity.status(HttpStatus.OK).body(username + " logged in successfully...");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).
                    body("UserName & Password doesn't match");
        }
    }
    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream().map(this::mapToUserResponse).toList();
    }

    public List<UserResponseDto> getUserByCNIC(String CNIC){
        List<User> usersByCNIC = userRepository.findByUserCNIC(CNIC);

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

    public List<Candidate> getAllCandidatesByConstituency(String userCNIC) {
        try {
            List<User> userOptional = userRepository.findByUserCNIC(userCNIC);
            if (!userOptional.isEmpty()) {
                User user = userOptional.get(0);
                Optional<Candidate> existingCandidate = candidateRepository.findByUser(user);
                if (existingCandidate.isPresent()){
                    String userConstituency = user.getUserConstituency();
                    List<Candidate> candidatesWithSameConstituency = candidateRepository.findByUser_UserConstituency(userConstituency);

                    return candidatesWithSameConstituency.stream()
                            .map(this::mapToCandidateResponse)
                            .collect(Collectors.toList());
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Candidate> getAllCandidates() {
        List<Candidate> candidates = candidateRepository.findAll();

        return candidates.stream().map(this::mapToCandidateResponse).toList();
    }

    private Candidate mapToCandidateResponse(Candidate candidate) {
        return Candidate.builder()
                .candidateID(candidate.getCandidateID())
                .candidateSymbolImage(candidate.getCandidateSymbolImage())
                .candidatePartyName(candidate.getCandidatePartyName())
                .build();
    }


}
