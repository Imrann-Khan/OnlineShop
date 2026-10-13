package org.example.shop.analytics;

import org.example.shop.order.OrderPlacedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Component
class SalesAnalyticsListener {

    private static final Logger log = LoggerFactory.getLogger(SalesAnalyticsListener.class);

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void onOrderPlaced(OrderPlacedEvent event) {
        log.info("[ANALYTICS] sale recorded: order #{} total=${} products={}",
                event.orderId(), event.total(), event.productIds());
    }
}