package com.voting.system.adminservice;

import com.voting.system.adminservice.model.Role;
import com.voting.system.adminservice.model.User;
import com.voting.system.adminservice.repository.RoleRepository;
import com.voting.system.adminservice.repository.UserRepository;
import com.voting.system.adminservice.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.Collections;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class AdminServiceApplication implements CommandLineRunner {


	@Autowired
	private AdminService adminService;

	public static void main(String[] args) {
		SpringApplication.run(AdminServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		adminService.seedAdminIfNotExist();
	}
}



