package com.example.springsecprac.service;

import com.example.springsecprac.model.AppUser;
import com.example.springsecprac.repository.UserRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public AppUser createUser(OidcUser user) {
        return repo.createUser(user);
    }

    public Optional<AppUser> getUserBySubject(String userSubject) {
        return repo.getUserBySubject(userSubject);
    }
}
