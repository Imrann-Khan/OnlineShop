package org.example.shop.user;

import jakarta.validation.Valid;
import org.example.shop.security.CurrentUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

// Self-service endpoints for the logged-in user; admin operations live in AdminUserController
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal Jwt jwt) {
        return service.findById(CurrentUser.id(jwt));
    }

    @PutMapping("/me")
    public UserResponse update(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody UpdateUserRequest request) {
        return service.update(CurrentUser.id(jwt), request);
    }
}
