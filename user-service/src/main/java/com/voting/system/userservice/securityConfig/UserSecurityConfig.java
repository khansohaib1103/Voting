package com.voting.system.userservice.securityConfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Data
public class UserSecurityConfig {

    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    public UserSecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    public UserSecurityConfig() {
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
                .antMatchers(HttpMethod.POST, "/api/saveUser").permitAll() // Require authentication for POST requests
                .antMatchers(HttpMethod.POST, "/api/login").permitAll()// Require authentication for POST requests
                .antMatchers(HttpMethod.GET, "/api/getUserByCNIC/{userCNIC}").permitAll() // Require authentication for POST requests
                .antMatchers(HttpMethod.POST, "/api/**").authenticated() // Require authentication for POST requests
                .antMatchers(HttpMethod.GET, "/api/**").authenticated()
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
