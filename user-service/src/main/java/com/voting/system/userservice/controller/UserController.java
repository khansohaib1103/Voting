package com.voting.system.userservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voting.system.userservice.dto.LoginDto;
import com.voting.system.userservice.dto.UserRequestDto;
import com.voting.system.userservice.dto.UserResponseDto;
import com.voting.system.userservice.model.Candidate;
import com.voting.system.userservice.model.User;
import com.voting.system.userservice.repository.RoleRepository;
import com.voting.system.userservice.repository.UserRepository;
import com.voting.system.userservice.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
/*import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;*/
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/saveUser")
    public ResponseEntity<String> saveUser(@RequestParam("file") MultipartFile file,
                                           @RequestParam("userRequestDto") String userRequestDtoJson) throws JsonProcessingException {

        UserRequestDto userRequestDto = new ObjectMapper().readValue(userRequestDtoJson, UserRequestDto.class);

        return userService.createUser(userRequestDto, file);

    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam("loginDto") String  loginDtoJSON) throws JsonProcessingException{
        LoginDto loginDto = new ObjectMapper().readValue(loginDtoJSON, LoginDto.class);
        return userService.loginUser(loginDto);
    }

    @GetMapping("/getAllUsers")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(value = "/getUserByCNIC/{userCNIC}")
    public List<UserResponseDto> getUserByCNIC(@PathVariable("userCNIC") String userCNIC){
        return userService.getUserByCNIC(userCNIC);
    }

    @GetMapping("/getAllCandidates")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    List<Candidate> getAllCandidates(){
        return userService.getAllCandidates();
    }

    @GetMapping("/getAllCandidates/{userCNIC}")
    @ResponseStatus(HttpStatus.OK)
    List<Candidate> getAllCandidatesByConstituency(@PathVariable("userCNIC")String userCNIC){
        return userService.getAllCandidatesByConstituency(userCNIC);
    }


}
