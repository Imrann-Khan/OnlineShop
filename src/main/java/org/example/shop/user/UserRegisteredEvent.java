package org.example.shop.user;

public record UserRegisteredEvent(
        Long userId,
        String name,
        String email,
        String phone
) {}
