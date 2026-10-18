package org.example.shop.security;

public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresInSeconds
) {}
