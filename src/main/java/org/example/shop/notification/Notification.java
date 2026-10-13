package org.example.shop.notification;

public record Notification(
        String recipientName,
        String email,
        String phone,
        String subject,
        String message
) {}
