package org.example.shop.notification;

import org.example.shop.config.ShopProperties;
import org.example.shop.notification.sdk.QuickSmsClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
class NotifierFactory {

    // A factory method: it decides WHICH classes to create, based on configuration.
    @Bean
    Notifier notifier(ShopProperties props) {
        List<Notifier> channels = props.notifications().channels().stream()
                .map(channel -> create(channel, props))
                .toList();
        return new MultiChannelNotifier(channels);
    }

    private Notifier create(String channel, ShopProperties props) {
        return switch (channel.trim().toLowerCase()) {
            case "console" -> new ConsoleNotifier();
            case "email" -> new EmailNotifier();
            case "sms" -> {
                String apiKey = props.notifications().smsApiKey();
                if (apiKey == null || apiKey.isBlank()) {
                    throw new IllegalStateException("shop.notifications.sms-api-key is required for the sms channel");
                }
                yield new QuickSmsAdapter(new QuickSmsClient(apiKey));
            }
            default -> throw new IllegalStateException("Unknown notification channel: " + channel);
        };
    }
}
