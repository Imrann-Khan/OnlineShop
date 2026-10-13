package org.example.shop.order.receipt;


import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

class HtmlReceiptBuilder implements ReceiptBuilder {

    private static final DateTimeFormatter DATE =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.systemDefault());
    private StringBuilder out = new StringBuilder();

    public void reset() { out = new StringBuilder(); }

    public void addHeader(String shopName, Long orderId, Instant createdAt) {
        out.append("<html><body><h1>").append(HtmlUtils.htmlEscape(shopName)).append(" receipt</h1>")
                .append("<p>Order #").append(orderId).append(" · ").append(DATE.format(createdAt)).append("</p>")
                .append("<table border=\"1\" cellpadding=\"6\"><tr><th>Product</th><th>Qty</th><th>Price</th><th>Total</th></tr>");
    }

    public void addLine(String product, int quantity, BigDecimal unitPrice, BigDecimal lineTotal) {
        out.append("<tr><td>").append(HtmlUtils.htmlEscape(product)).append("</td><td>").append(quantity)
                .append("</td><td>").append(unitPrice).append("</td><td>").append(lineTotal).append("</td></tr>");
    }

    public void addSummary(BigDecimal subtotal, String promoCode, BigDecimal discount, BigDecimal total) {
        out.append("</table><p>Subtotal: ").append(subtotal).append("</p>");
        if (promoCode != null) {
            out.append("<p>Discount (").append(HtmlUtils.htmlEscape(promoCode)).append("): -").append(discount).append("</p>");
        }
        out.append("<p><strong>Total: ").append(total).append("</strong></p>");
    }

    public void addFooter(String message) {
        out.append("<p>").append(HtmlUtils.htmlEscape(message)).append("</p></body></html>");
    }

    public String getResult() { return out.toString(); }
}