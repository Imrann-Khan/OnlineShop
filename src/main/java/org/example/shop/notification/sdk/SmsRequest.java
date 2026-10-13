package org.example.shop.notification.sdk;

public record SmsRequest(
        String phone,
        String content
) {}

