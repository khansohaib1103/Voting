package com.voting.system.votingservice.securityConfig;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Data
public class VotingSecurityConfig {

    private CustomVotingDetailsService customVotingDetailsService;

    @Autowired
    public VotingSecurityConfig(CustomVotingDetailsService customVotingDetailsService) {
        this.customVotingDetailsService = customVotingDetailsService;
    }

    public VotingSecurityConfig() {
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.
                csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/vote").authenticated()  // Require authentication for POST requests
                .antMatchers(HttpMethod.POST, "/api/**").permitAll() // Require authentication for POST requests
                .antMatchers(HttpMethod.GET, "/api/**").permitAll()
                .anyRequest().permitAll()
                .and()
                .httpBasic();

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
}
