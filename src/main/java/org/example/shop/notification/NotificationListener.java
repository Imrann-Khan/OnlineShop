package org.example.shop.notification;

import org.example.shop.order.OrderPlacedEvent;
import org.example.shop.user.UserRegisteredEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
class NotificationListener {

    private final Notifier notifier;

    NotificationListener(Notifier notifier) {
        this.notifier = notifier;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void onUserRegistered(UserRegisteredEvent e) {
        notifier.send(new Notification(e.name(), e.email(), e.phone(),
                "Welcome!", "Hi " + e.name() + ", your account is ready."));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void onOrderPlaced(OrderPlacedEvent e) {
        notifier.send(new Notification(e.customerName(), e.customerEmail(), e.customerPhone(),
                "Order #" + e.orderId() + " confirmed", "Thank you! Total: $" + e.total()));
    }
}