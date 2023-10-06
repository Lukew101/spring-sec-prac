package com.example.springsecprac.repository;

import com.example.springsecprac.model.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

    private final UserJPARepository userJPARepository;

    public UserRepository(UserJPARepository userJPARepository) {
        this.userJPARepository = userJPARepository;
    }

    public UserDetails createUser(OidcUser userToSave) {
        UserDetails user = new UserDetails(
                userToSave.getSubject(),
                userToSave.getGivenName(),
                userToSave.getFamilyName(),
                userToSave.getEmail(),
                userToSave.getPicture()
        );
        return userJPARepository.save(user);
    }

    public Optional<UserDetails> getUserBySubject(String userSubject) {
        return userJPARepository.findBySubject(userSubject);
    }
}
