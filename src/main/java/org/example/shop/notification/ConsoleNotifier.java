package org.example.shop.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "shop.notifications.channel", havingValue = "console", matchIfMissing = true)
class ConsoleNotifier implements Notifier {

    private static final Logger log = LoggerFactory.getLogger(ConsoleNotifier.class);

    @Override
    public void send(String to, String subject, String message) {
        log.info("[CONSOLE] to={} subject='{}' message='{}'", to, subject, message);
    }
}
