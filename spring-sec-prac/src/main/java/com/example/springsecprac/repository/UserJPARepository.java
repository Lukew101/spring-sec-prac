package com.example.springsecprac.repository;

import com.example.springsecprac.model.AppUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserJPARepository extends CrudRepository<AppUser, String> {

    Optional<AppUser> findBySubject(String userSubject);

    AppUser findByEmail(String email);
}
