package org.example.shop.discount;

import org.example.shop.common.BadRequestException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DiscountService {

    private final Map<String, DiscountStrategy> strategiesByCode;

    public DiscountService(List<DiscountStrategy> strategies) {
        this.strategiesByCode = strategies.stream()
                .collect(Collectors.toUnmodifiableMap(s -> s.code().toUpperCase(), Function.identity()));
    }

    public BigDecimal calculate(String promoCode, DiscountInput input) {
        if (promoCode == null || promoCode.isBlank()) {
            return BigDecimal.ZERO;
        }
        DiscountStrategy strategy = strategiesByCode.get(promoCode.trim().toUpperCase());
        if (strategy == null) {
            throw new BadRequestException("Unknown promo code: " + promoCode);
        }
        return strategy.discountFor(input).min(input.subtotal());
    }
}
