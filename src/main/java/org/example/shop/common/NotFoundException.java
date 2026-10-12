package org.example.shop.common;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message)
    {
        super(message);
    }
}