package org.example.shop.order.receipt;

import org.example.shop.order.OrderResponse;
import org.springframework.stereotype.Component;

@Component
class ReceiptDirector {

    void buildReceipt(OrderResponse order, ReceiptBuilder builder) {
        builder.reset();
        builder.addHeader("Shop", order.id(), order.createdAt());
        order.items().forEach(line ->
                builder.addLine(line.productName(), line.quantity(), line.unitPrice(), line.lineTotal()));
        builder.addSummary(order.subtotal(), order.promoCode(), order.discount(), order.total());
        builder.addFooter("Thank you for shopping with us!");
    }
}
