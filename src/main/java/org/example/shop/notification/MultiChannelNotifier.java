package org.example.shop.notification;


import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.List;

class MultiChannelNotifier implements Notifier {

    private static final Logger log = (Logger) LoggerFactory.getLogger(MultiChannelNotifier.class);
    private final List<Notifier> channels;

    MultiChannelNotifier(List<Notifier> channels) {
        this.channels = List.copyOf(channels);
    }

    @Override
    public void send(Notification notification) {
        for (Notifier channel : channels) {
            try {
                channel.send(notification);
            } catch (RuntimeException e) {          // one broken channel must not stop the others
                log.warn("Channel {} failed: {}", channel.getClass().getSimpleName(), e.getMessage());
            }
        }
    }
}
