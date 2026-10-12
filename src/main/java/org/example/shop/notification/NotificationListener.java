package org.example.shop.notification;

import org.example.shop.order.OrderPlacedEvent;
import org.example.shop.user.UserRegisteredEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
class NotificationListeners {

    private final Notifier notifier;

    NotificationListeners(Notifier notifier) {
        this.notifier = notifier;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void onUserRegistered(UserRegisteredEvent event) {
        notifier.send(event.email(), "Welcome!", "Hi " + event.name() + ", your account is ready.");
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void onOrderPlaced(OrderPlacedEvent event) {
        notifier.send(event.customerEmail(),
                "Order #" + event.orderId() + " confirmed",
                "Thank you! Total: $" + event.total());
    }
}