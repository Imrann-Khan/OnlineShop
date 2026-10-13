package org.example.shop.order.receipt;


import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

class TextReceiptBuilder implements ReceiptBuilder {

    private static final DateTimeFormatter DATE =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.systemDefault());
    private StringBuilder out = new StringBuilder();

    public void reset() { out = new StringBuilder(); }

    public void addHeader(String shopName, Long orderId, Instant createdAt) {
        out.append(shopName.toUpperCase()).append(" - RECEIPT\n")
                .append("Order #").append(orderId).append("   ").append(DATE.format(createdAt)).append("\n")
                .append("-".repeat(48)).append("\n");
    }

    public void addLine(String product, int quantity, BigDecimal unitPrice, BigDecimal lineTotal) {
        out.append(String.format("%-20.20s %3d x %8.2f = %9.2f%n", product, quantity, unitPrice, lineTotal));
    }

    public void addSummary(BigDecimal subtotal, String promoCode, BigDecimal discount, BigDecimal total) {
        out.append("-".repeat(48)).append("\n")
                .append(String.format("%-36s %11.2f%n", "Subtotal", subtotal));
        if (promoCode != null) {
            out.append(String.format("%-36s %11.2f%n", "Discount (" + promoCode + ")", discount.negate()));
        }
        out.append(String.format("%-36s %11.2f%n", "TOTAL", total));
    }

    public void addFooter(String message) {
        out.append("\n").append(message).append("\n");
    }

    public String getResult() { return out.toString(); }
}