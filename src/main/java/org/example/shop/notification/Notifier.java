package org.example.shop.notification;

public interface Notifier {
    void send(String to, String subject, String message);
}