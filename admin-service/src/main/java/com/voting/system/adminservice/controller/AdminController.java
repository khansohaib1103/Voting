package com.voting.system.adminservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voting.system.adminservice.dto.AdminResponseDto;
import com.voting.system.adminservice.dto.LoginDto;
import com.voting.system.adminservice.model.User;
import com.voting.system.adminservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminController {

    @Autowired
    private final AdminService adminService;


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam("loginDto") String  loginDtoJSON) throws JsonProcessingException{
        LoginDto loginDto = new ObjectMapper().readValue(loginDtoJSON, LoginDto.class);
        return adminService.loginUser(loginDto);
    }


    @GetMapping("/getAllUsers")
    @ResponseStatus(HttpStatus.OK)
    public List<AdminResponseDto> getAllUsers() {
        return adminService.getAllUsers();
    }

    @GetMapping(value = "/getUserByCNIC/{userCNIC}")
    public List<AdminResponseDto> getUserByCNIC(@PathVariable("userCNIC") String userCNIC){
        return adminService.getUserByCNIC(userCNIC);
    }



    @PostMapping("/approveUser/{userCNIC}")
    Boolean validateUser(@PathVariable("userCNIC") String userCNIC){
         return adminService.validateUser(userCNIC);
    }

    @PostMapping("/approveCandidate/{userCNIC}")
    Boolean validateCandidate(@PathVariable("userCNIC") String userCNIC){
        return adminService.validateCandidate(userCNIC);
    }

    @PostMapping("/election")
    public ResponseEntity<String> electionDateAndTime(@RequestParam("startDateAndTime") String startDateAndTimeStr,
                                                      @RequestParam("endDateAndTime") String endDateAndTimeStr) {
            LocalDateTime startDateAndTime = LocalDateTime.parse(startDateAndTimeStr);
            LocalDateTime endDateAndTime = LocalDateTime.parse(endDateAndTimeStr);

            return adminService.electionDateAndTime(startDateAndTime, endDateAndTime);

    }
}
