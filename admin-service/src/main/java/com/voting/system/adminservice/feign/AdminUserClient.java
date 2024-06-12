package com.voting.system.adminservice.feign;

import com.voting.system.adminservice.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@FeignClient("USER-SERVICE")
public interface AdminUserClient {

    @GetMapping("/getAllUsers")
    @ResponseStatus(HttpStatus.OK)
     List<User> getAllUsers();

    @GetMapping(value = "/getUserByCNIC/{userCNIC}")
     List<User> getUserByCNIC(@PathVariable("userCNIC") String CNIC);

}
