package org.example.shop.order.receipt;


import java.math.BigDecimal;
import java.time.Instant;

public interface ReceiptBuilder {
    void reset();
    void addHeader(String shopName, Long orderId, Instant createdAt);
    void addLine(String product, int quantity, BigDecimal unitPrice, BigDecimal lineTotal);
    void addSummary(BigDecimal subtotal, String promoCode, BigDecimal discount, BigDecimal total);
    void addFooter(String message);
}
