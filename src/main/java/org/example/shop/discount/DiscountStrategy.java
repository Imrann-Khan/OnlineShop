package org.example.shop.discount;



import org.example.shop.common.BadRequestException;

import java.math.BigDecimal;

public interface DiscountStrategy {
    String code();
    BigDecimal discountFor(DiscountInput input) throws BadRequestException;
}