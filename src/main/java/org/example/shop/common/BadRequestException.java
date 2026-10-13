package org.example.shop.common;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) { super(message); }
}
