package org.example.shop.security;

import jakarta.validation.Valid;
import org.example.shop.user.RegisterRequest;
import org.example.shop.user.UserResponse;
import org.example.shop.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
class AuthController {

    private final UserService users;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokens;

    AuthController(UserService users, AuthenticationManager authenticationManager, TokenService tokens) {
        this.users = users;
        this.authenticationManager = authenticationManager;
        this.tokens = tokens;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    UserResponse register(@Valid @RequestBody RegisterRequest request) {
        return users.register(request);
    }

    @PostMapping("/login")
    LoginResponse login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        return tokens.issue(authentication);
    }
}