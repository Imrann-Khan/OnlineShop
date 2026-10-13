package org.example.shop.discount;

import java.math.BigDecimal;

public record DiscountInput(
        BigDecimal subtotal,
        int totalQuantity
) {}