package com.example.springsecprac.service;


import com.example.springsecprac.dao.JwtAuthResponse;
import com.example.springsecprac.dao.SignInRequest;
import com.example.springsecprac.model.Role;
import com.example.springsecprac.model.AppUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.springsecprac.dao.SignUpRequest;
import com.example.springsecprac.repository.UserRepository;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public JwtAuthResponse signup(SignUpRequest request) {
        AppUser user = new AppUser(request.getName(), passwordEncoder.encode(request.getPassword()), request.getEmail(), Role.USER);

        userRepository.createUser(user);
        var jwt = jwtService.generateToken(user);
        return JwtAuthResponse.builder().token(jwt).build();
    }


    public JwtAuthResponse signin(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        AppUser user = userRepository.findByEmail(request.getEmail());
        String jwt = null;
        if(user != null){
            jwt = jwtService.generateToken(user);
        }else{
            throw new IllegalArgumentException("User not found");
        }
        return JwtAuthResponse.builder().token(jwt).build();
    }
}
