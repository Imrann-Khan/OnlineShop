package org.example.shop.catelog;


import org.example.shop.common.ConflictException;

public class OutOfStockException extends ConflictException {   // reuses your 409 handler
    public OutOfStockException(String product, int available, int requested) {
        super("Not enough stock for '" + product + "': requested " + requested + ", available " + available);
    }
}
