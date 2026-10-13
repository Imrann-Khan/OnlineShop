package org.example.shop.discount;

import org.example.shop.common.BadRequestException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
class BulkDiscount implements DiscountStrategy {

    @Override
    public String code() { return "BULK"; }

    @Override
    public BigDecimal discountFor(DiscountInput input) throws BadRequestException {
        if (input.totalQuantity() < 5) {
            throw new BadRequestException("Promo code BULK needs at least 5 items");
        }
        return input.subtotal().multiply(new BigDecimal("0.15")).setScale(2, RoundingMode.HALF_UP);
    }
}