package com.voting.system.adminservice.service;

import com.voting.system.adminservice.dto.AdminResponseDto;
import com.voting.system.adminservice.dto.LoginDto;
import com.voting.system.adminservice.feign.AdminUserClient;
import com.voting.system.adminservice.model.Candidate;
import com.voting.system.adminservice.model.Polling;
import com.voting.system.adminservice.model.Role;
import com.voting.system.adminservice.model.User;
import com.voting.system.adminservice.repository.PollingRepository;
import com.voting.system.adminservice.repository.RoleRepository;
import com.voting.system.adminservice.repository.UserRepository;
import com.voting.system.adminservice.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    @Autowired
    private AdminUserClient adminUserClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PollingRepository pollingRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

   public void seedAdminIfNotExist() {
       String userCNIC = "12345-1234567-8";
       if (!userRepository.existsByUserCNIC(userCNIC)) {

           User user = User.builder()
                   .userCNIC(userCNIC)
                   .userName("Admin")
                   .userConstituency("Islamabad")
                   .userPassword(passwordEncoder.encode("Nanan@1234"))
                   .build();


           Role roles = roleRepository.findByRoleName("ADMIN").get();

           System.out.println(" " + roles);
           user.setRole(Collections.singletonList(roles));
           userRepository.save(user);
       }
       else {
           System.out.println("Admin already exists...");
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
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUserName(),
                            loginDto.getUserPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.status(HttpStatus.OK).body(username + " logged in successfully...");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).
                    body("UserName & Password doesn't match");
        }
    }

    public List<AdminResponseDto> getAllUsers() {
        List<User> users = adminUserClient.getAllUsers();

        return users.stream().map(this::mapToUserResponse).toList();
    }

    public List<AdminResponseDto> getUserByCNIC(String CNIC){
        List<User> usersByCNIC = adminUserClient.getUserByCNIC(CNIC);
        return usersByCNIC.stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    public Boolean validateUser(String CNIC) {
        Optional<User> userOptional = userRepository.findByUserCNIC(CNIC);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userRepository.save(user);
            log.info("User with CNIC {} not Approved", CNIC);
            return false;
        } else {
            log.info("User with CNIC {} Approved", CNIC);
            return true;
        }
    }

    public Boolean validateCandidate(String userCNIC) {
        Optional<User> userOptional = userRepository.findByUserCNIC(userCNIC);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Candidate> candidateOptional = candidateRepository.findByUser(user);

            if (candidateOptional.isPresent()) {
                log.info("Candidate with CNIC {} already exists", userCNIC);
                return false;
            } else {
                log.info("Candidate with CNIC {} does not exist", userCNIC);
                return true;
            }
        } else {
            log.info("User with CNIC {} not found", userCNIC);
            return true;
        }
    }


    private AdminResponseDto mapToUserResponse(User user) {
        return AdminResponseDto.builder()
                .userID(user.getUserID())
                .userName(user.getUserName())
                .userCNIC(user.getUserCNIC())
                .userConstituency(user.getUserConstituency())
                .userImage(user.getUserImage())
                .build();
    }

    public ResponseEntity<String> electionDateAndTime(LocalDateTime startDateAndTime, LocalDateTime endDateAndTime) {

       LocalDateTime currentDateTime = LocalDateTime.now();

        if (startDateAndTime.isBefore(currentDateTime)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start date and time should be ahead of the current date and time");
        }

        if (endDateAndTime.isBefore(startDateAndTime)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("End date and time should be ahead of the start date and time");
        }

        List<Polling> existingPollings = pollingRepository.findAll();
        for (Polling polling : existingPollings) {
            LocalDateTime existingStartDateTime = polling.getStartDateTime();
            LocalDateTime existingEndDateTime = polling.getEndDateTime();

            /*if (startDateAndTime.isBefore(existingEndDateTime) && endDateAndTime.isAfter(existingStartDateTime)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is already a polling scheduled for the provided date and time");
            }*/
        }

        try {
            Polling polling = Polling.builder()
                    .startDateTime(startDateAndTime.truncatedTo(ChronoUnit.MINUTES))
                    .endDateTime(endDateAndTime.truncatedTo(ChronoUnit.MINUTES))
                    .build();
            pollingRepository.save(polling);
            return ResponseEntity.ok("Election date and time set successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error setting election date and time");
        }
    }
}
