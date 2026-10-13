package org.example.shop.discount;

import java.math.BigDecimal;
import java.math.RoundingMode;

class PercentOffDiscount implements DiscountStrategy {

    private final String code;
    private final BigDecimal percent;

    PercentOffDiscount(String code, int percent) {
        this.code = code;
        this.percent = BigDecimal.valueOf(percent);
    }

    @Override
    public String code() { return code; }

    @Override
    public BigDecimal discountFor(DiscountInput input) {
        return input.subtotal().multiply(percent).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
