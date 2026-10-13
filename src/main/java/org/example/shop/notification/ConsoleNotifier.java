package org.example.shop.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

class ConsoleNotifier implements Notifier {

    private static final Logger log = LoggerFactory.getLogger(ConsoleNotifier.class);

    @Override
    public void send(Notification n) {
        log.info("[CONSOLE] to {} <{}> | {} | {}", n.recipientName(), n.email(), n.subject(), n.message());
    }
}
