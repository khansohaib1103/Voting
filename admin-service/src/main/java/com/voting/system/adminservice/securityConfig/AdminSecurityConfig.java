package com.voting.system.adminservice.securityConfig;

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
public class AdminSecurityConfig {

    private CustomAdminDetailsService customAdminDetailsService;

    @Autowired
    public AdminSecurityConfig(CustomAdminDetailsService customAdminDetailsService) {
        this.customAdminDetailsService = customAdminDetailsService;
    }

    public AdminSecurityConfig() {
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
                .antMatchers(HttpMethod.POST, "/api/saveAdmin").permitAll()
                .antMatchers(HttpMethod.POST, "/api/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/approveUser/{userCNIC}").permitAll()
                .antMatchers(HttpMethod.POST, "/api/approveCandidate/{userCNIC}").permitAll()
                .antMatchers(HttpMethod.POST, "/api/**").authenticated()
                .antMatchers(HttpMethod.GET, "/api/**").authenticated()
                .anyRequest().authenticated()
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

