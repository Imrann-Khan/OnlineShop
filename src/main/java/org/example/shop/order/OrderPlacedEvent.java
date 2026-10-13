package org.example.shop.order;

import java.math.BigDecimal;
import java.util.List;

public record OrderPlacedEvent(
        Long orderId,
        String customerName,
        String customerEmail,
        String customerPhone,
        BigDecimal total,
        List<Long> productIds
) {}