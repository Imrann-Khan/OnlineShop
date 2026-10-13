package org.example.shop.notification.sdk;

public final class QuickSmsClient {

    private final String apiKey;

    public QuickSmsClient(String apiKey) {
        this.apiKey = apiKey;
    }

    /** Returns 200 = sent, 400 = bad request (e.g. longer than 160 characters). */
    public int dispatch(SmsRequest request) {
        if (request.content().length() > 160) {
            return 400;
        }
        System.out.println("[QuickSms] to " + request.phone() + ": " + request.content());
        return 200;
    }
}
