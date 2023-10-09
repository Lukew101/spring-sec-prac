package com.example.springsecprac.controller;

import com.example.springsecprac.model.UserDetails;
import com.example.springsecprac.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomeController {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String home() {
        return "Hello, home!";
    }

    @GetMapping("/secured")
    public String update() {
        return "Hello! Only authorized users can see this";
    }

    @GetMapping("user")
    public ResponseEntity<UserDetails> getUser(@AuthenticationPrincipal OidcUser user) {
        var currentUser = userService.getUserBySubject(user.getSubject());
        return ResponseEntity.of(currentUser);
    }
}
