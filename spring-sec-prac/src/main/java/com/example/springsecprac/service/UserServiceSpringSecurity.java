package com.example.springsecprac.service;

import com.example.springsecprac.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceSpringSecurity {

    private final UserRepository userRepository;

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                Optional<UserDetails> userOptional = Optional.ofNullable(userRepository.findByEmail(username));
                return userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }
}