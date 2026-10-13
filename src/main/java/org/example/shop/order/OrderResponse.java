package org.example.shop.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(Long id, Long customerId, OrderStatus status, Instant createdAt,
                            List<Line> items, BigDecimal subtotal, String promoCode,
                            BigDecimal discount, BigDecimal total) {

    public record Line(Long productId, String productName, int quantity, BigDecimal unitPrice, BigDecimal lineTotal) {}

    static OrderResponse from(Order o) {
        List<Line> lines = o.getItems().stream()
                .map(i -> new Line(i.getProduct().getId(), i.getProduct().getName(),
                        i.getQuantity(), i.getUnitPrice(), i.lineTotal()))
                .toList();
        return new OrderResponse(
                o.getId(),
                o.getCustomer().getId(),
                o.getStatus(),
                o.getCreatedAt(),
                lines,
                o.subtotal(),
                o.getPromoCode(),
                o.getDiscount(),
                o.total()
        );
    }
}
