package org.example.shop.order;

import java.math.BigDecimal;

public record OrderPlacedEvent(
        Long orderId,
        String customerEmail,
        BigDecimal total
) {}