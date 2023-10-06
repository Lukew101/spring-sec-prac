package com.example.springsecprac.security;

import com.example.springsecprac.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtValidation validation;
    private final UserService userService;

    @Autowired
    public CustomAuthenticationSuccessHandler(JwtValidation validation, UserService userService){
        this.validation = validation;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String tokenValue = oidcUser.getIdToken().getTokenValue();

        try {
            validation.validateJWTString(tokenValue);
        } catch (JwtValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
        String redirectUrl = "http://localhost:3000";
        createUserAccountIfItDoesNotExist(oidcUser);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.addCookie(createNewCookie(oidcUser.getIdToken().getTokenValue()));
        response.sendRedirect(redirectUrl);
    }

    private void createUserAccountIfItDoesNotExist(@AuthenticationPrincipal OidcUser oidcUser){
        if (userService.getUserBySubject(oidcUser.getSubject()).isEmpty()) {
            userService.createUser(oidcUser);
        }
    }

    private Cookie createNewCookie(String tokenValue) {
        Cookie cookie = new Cookie("JwtToken", tokenValue);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(3500);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setDomain("localhost");
        return cookie;
    }
}
