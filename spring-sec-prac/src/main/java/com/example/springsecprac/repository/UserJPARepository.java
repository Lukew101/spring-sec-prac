package com.example.springsecprac.repository;

import com.example.springsecprac.model.UserDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserJPARepository extends CrudRepository<UserDetails, String> {

    Optional<UserDetails> findBySubject(String userSubject);
}
