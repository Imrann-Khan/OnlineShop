package org.example.shop.catelog;

import org.example.shop.order.OrderPlacedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Component
class LowStockListener {

    private static final Logger log = LoggerFactory.getLogger(LowStockListener.class);
    private static final int THRESHOLD = 3;
    private final ProductService products;

    LowStockListener(ProductService products) {
        this.products = products;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void onOrderPlaced(OrderPlacedEvent event) {
        products.findLowStock(event.productIds(), THRESHOLD)
                .forEach(p -> log.warn("[LOW STOCK] '{}' has only {} left", p.name(), p.stock()));
    }
}
