package com.example.springsecprac.security;

import com.example.springsecprac.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class CustomBearerTokenFilter extends OncePerRequestFilter {

    private final JwtValidation validation;
    private static final String JWT_TOKEN_COOKIE_NAME = "JwtToken";
    private final UserRepository userRepository;

    CustomBearerTokenFilter(JwtValidation validation, UserRepository userRepository) {
        this.validation = validation;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        var jwtTokenString = getJwtTokenFromRequestCookie(request);

        if (jwtTokenString.isPresent()) {
            System.out.println(jwtTokenString.get());
            try {
                Jwt jwt = validation.validateJWTString(jwtTokenString.get());
                OidcUser user = createOidcUserFromJwt(jwt);
                redirectUserWithNoRegisteredAccountOtherWiseSetAuthenticated(user, response);
            } catch (JwtException e) {
                System.out.println(e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: " + e.getMessage());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private Optional<String> getJwtTokenFromRequestCookie(HttpServletRequest request) {
        Optional<Cookie[]> cookies = Optional.ofNullable(request.getCookies());
        return cookies.stream().flatMap(Stream::of)
                .filter(cookie -> JWT_TOKEN_COOKIE_NAME.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue);

    }

    private OidcUser createOidcUserFromJwt(Jwt jwt) {
        OidcIdToken oidcIdToken =
                new OidcIdToken(jwt.getTokenValue(), jwt.getIssuedAt(), jwt.getExpiresAt(), jwt.getClaims());
        return new DefaultOidcUser(Collections.emptyList(), oidcIdToken);
    }

    private void redirectUserWithNoRegisteredAccountOtherWiseSetAuthenticated
            (OidcUser user, HttpServletResponse response) throws IOException {
        var echoBoardUser = userRepository.getUserBySubject(user.getSubject());
        if (echoBoardUser.isEmpty()) {
            response.sendRedirect("http://localhost:3000");
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }
}