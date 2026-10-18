package org.example.shop.security;

import jakarta.validation.Valid;
import org.example.shop.common.TooManyRequestsException;
import org.example.shop.user.RegisterRequest;
import org.example.shop.user.UserResponse;
import org.example.shop.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/api/auth")
class AuthController {

    private final UserService users;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokens;
    private final LoginRateLimiter rateLimiter;

    AuthController(UserService users, AuthenticationManager authenticationManager, TokenService tokens, LoginRateLimiter rateLimiter) {
        this.users = users;
        this.authenticationManager = authenticationManager;
        this.tokens = tokens;
        this.rateLimiter = rateLimiter;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    UserResponse register(@Valid @RequestBody RegisterRequest request) {
        return users.register(request);
    }

    @PostMapping("/login")
    LoginResponse login(@Valid @RequestBody LoginRequest request) {
        long wait = rateLimiter.secondsUntilAllowed(request.email());
        if (wait > 0) {
            throw new TooManyRequestsException("Too many failed login attempts. Try again later.", wait);
        }

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        } catch (TooManyRequestsException e) {
            rateLimiter.recordFailure(request.email());
            throw e;
        }

        rateLimiter.reset(request.email());
        return tokens.issue(authentication);
    }
}