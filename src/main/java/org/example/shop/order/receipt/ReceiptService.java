package org.example.shop.order.receipt;

import org.example.shop.common.BadRequestException;
import org.example.shop.order.OrderResponse;
import org.example.shop.order.OrderService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class ReceiptService {

    private final OrderService orders;
    private final ReceiptDirector director;

    public ReceiptService(OrderService orders, ReceiptDirector director) {
        this.orders = orders;
        this.director = director;
    }

    public Receipt render(Long orderId, String format) {
        OrderResponse order = orders.getOrder(orderId);
        return switch (format.toLowerCase()) {
            case "text" -> {
                TextReceiptBuilder builder = new TextReceiptBuilder();     // a NEW builder per request
                director.buildReceipt(order, builder);
                yield new Receipt(builder.getResult(), MediaType.TEXT_PLAIN);
            }
            case "html" -> {
                HtmlReceiptBuilder builder = new HtmlReceiptBuilder();
                director.buildReceipt(order, builder);
                yield new Receipt(builder.getResult(), MediaType.TEXT_HTML);
            }
            default -> throw new BadRequestException("Unknown receipt format: " + format);
        };
    }

    public record Receipt(String content, MediaType mediaType) {}
}
