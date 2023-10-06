package com.example.springsecprac.service;

import com.example.springsecprac.model.UserDetails;
import com.example.springsecprac.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public UserDetails createUser(OidcUser user) {
        return repo.createUser(user);
    }

    public Optional<UserDetails> getUserBySubject(String userSubject) {
        return repo.getUserBySubject(userSubject);
    }
}
