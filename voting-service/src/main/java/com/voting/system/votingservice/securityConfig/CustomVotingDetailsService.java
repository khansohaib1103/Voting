package com.voting.system.votingservice.securityConfig;

import com.voting.system.votingservice.model.Role;
import com.voting.system.votingservice.model.User;
import com.voting.system.votingservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomVotingDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomVotingDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found."));
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getUserPassword(), mapRolesToAuthorities(user.getRole()));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
    }
}
