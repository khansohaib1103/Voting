package com.voting.system.candidateservice.securityConfig;

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
public class CandidateSecurityConfig {

    private CustomCandidateDetailsService customCandidateDetailsService;

    @Autowired
    public CandidateSecurityConfig(CustomCandidateDetailsService customCandidateDetailsService) {
        this.customCandidateDetailsService = customCandidateDetailsService;
    }

    public CandidateSecurityConfig() {
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
                .antMatchers("/api/saveCandidate").authenticated()
                .antMatchers("/api/**").permitAll()
                .and()
                .logout().and()
                .httpBasic();

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
}
