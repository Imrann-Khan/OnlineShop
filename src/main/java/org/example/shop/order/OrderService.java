package org.example.shop.order;

import org.example.shop.audit.AuditService;
import org.example.shop.catelog.Product;
import org.example.shop.catelog.ProductService;
import org.example.shop.common.NotFoundException;
import org.example.shop.user.User;
import org.example.shop.user.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orders;
    private final UserService users;
    private final ProductService products;
    private final AuditService audit;
    private final ApplicationEventPublisher events;

    public OrderService(OrderRepository orders, UserService users, ProductService products,
                        AuditService audit, ApplicationEventPublisher events) {
        this.orders = orders;
        this.users = users;
        this.products = products;
        this.audit = audit;
        this.events = events;
    }

    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest request) {
        audit.record("ORDER_ATTEMPT",
                "user=" + request.userId() + ", lines=" + request.items().size());

        User customer = users.findEntity(request.userId());
        Order order = new Order(customer);

        for (PlaceOrderRequest.Line line : request.items()) {
            Product product = products.reserveStock(line.productId(), line.quantity());
            order.addItem(product, line.quantity());
        }

        Order saved = orders.save(order);              // cascade: INSERT order + every item
        events.publishEvent(new OrderPlacedEvent(saved.getId(), customer.getEmail(), saved.total()));
        return OrderResponse.from(saved);
    }

    public OrderResponse getOrder(Long id) {
        return orders.findWithItemsById(id)
                .map(OrderResponse::from)
                .orElseThrow(() -> new NotFoundException("Order " + id + " not found"));
    }

    public List<OrderSummary> history(Long userId) {
        users.findEntity(userId);                      // 404 if the user doesn't exist
        return orders.findSummariesByCustomerId(userId);
    }
}