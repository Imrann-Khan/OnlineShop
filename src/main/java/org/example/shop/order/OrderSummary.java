package org.example.shop.order;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderSummary(
        Long id,
        OrderStatus status,
        Instant createdAt,
        Long itemCount,
        BigDecimal total
) {}
