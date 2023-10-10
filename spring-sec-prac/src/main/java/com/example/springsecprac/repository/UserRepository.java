package com.example.springsecprac.repository;

import com.example.springsecprac.model.AppUser;
import com.example.springsecprac.model.Role;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {

    private final UserJPARepository userJPARepository;

    public UserRepository(UserJPARepository userJPARepository) {
        this.userJPARepository = userJPARepository;
    }

    public AppUser createUser(OidcUser userToSave) {
        AppUser user = new AppUser(
                userToSave.getSubject(),
                userToSave.getFullName(),
                userToSave.getGivenName(),
                userToSave.getFamilyName(),
                userToSave.getEmail(),
                userToSave.getPicture(),
                Role.USER
        );
        return userJPARepository.save(user);
    }

    public AppUser createUser(AppUser user) {
        user.setSubject(UUID.randomUUID().toString());
       return userJPARepository.save(user);
    }

    public Optional<AppUser> getUserBySubject(String userSubject) {
        return userJPARepository.findBySubject(userSubject);
    }

    public AppUser findByEmail(String email) {
        return userJPARepository.findByEmail(email);
    }
}
