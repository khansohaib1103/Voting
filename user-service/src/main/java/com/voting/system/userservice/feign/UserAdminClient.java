package com.voting.system.userservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("ADMIN-SERVICE")
public interface UserAdminClient {
    @PostMapping("/api/approveUser/{userCNIC}")
    Boolean validateUser(@PathVariable("userCNIC") String CNIC);
}
