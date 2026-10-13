package org.example.shop.notification;


import org.example.shop.notification.sdk.QuickSmsClient;
import org.example.shop.notification.sdk.SmsRequest;

class QuickSmsAdapter implements Notifier {

    private static final int MAX_LENGTH = 160;
    private final QuickSmsClient client;

    QuickSmsAdapter(QuickSmsClient client) {
        this.client = client;
    }

    @Override
    public void send(Notification n) {
        if (n.phone() == null || n.phone().isBlank()) {
            return;
        }
        String phone = n.phone().replaceAll("[^0-9]", "");
        String text = n.subject() + ": " + n.message();
        String content = text.length() > MAX_LENGTH
                ? text.substring(0, MAX_LENGTH - 3) + "..."
                : text;
        int status = client.dispatch(new SmsRequest(phone, content));
        if (status != 200) {
            throw new IllegalStateException("SMS failed with status " + status);
        }
    }
}
