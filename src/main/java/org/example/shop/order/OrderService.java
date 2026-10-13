package org.example.shop.order;

import org.example.shop.audit.AuditService;
import org.example.shop.catelog.Product;
import org.example.shop.catelog.ProductService;
import org.example.shop.common.NotFoundException;
import org.example.shop.discount.DiscountInput;
import org.example.shop.discount.DiscountService;
import org.example.shop.user.User;
import org.example.shop.user.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orders;
    private final UserService users;
    private final ProductService products;
    private final AuditService audit;
    private final ApplicationEventPublisher events;
    private final DiscountService discounts;

    public OrderService(OrderRepository orders, UserService users, ProductService products,
                        AuditService audit, ApplicationEventPublisher events,  DiscountService discounts) {
        this.orders = orders;
        this.users = users;
        this.products = products;
        this.audit = audit;
        this.events = events;
        this.discounts = discounts;
    }

    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest request) {
        audit.record("ORDER_ATTEMPT", "user=" + request.userId() + ", lines=" + request.items().size());

        User customer = users.findEntity(request.userId());
        Order order = new Order(customer);
        for (PlaceOrderRequest.Line line : request.items()) {
            Product product = products.reserveStock(line.productId(), line.quantity());
            order.addItem(product, line.quantity());
        }

        // Strategy: the discount service picks the right rule for the code
        BigDecimal discount = discounts.calculate(request.promoCode(),
                new DiscountInput(order.subtotal(), order.totalQuantity()));
        String code = (request.promoCode() == null || request.promoCode().isBlank())
                ? null : request.promoCode().trim().toUpperCase();
        order.applyDiscount(code, discount);

        Order saved = orders.save(order);

        List<Long> productIds = request.items().stream().map(PlaceOrderRequest.Line::productId).distinct().toList();
        events.publishEvent(new OrderPlacedEvent(saved.getId(), customer.getName(), customer.getEmail(),
                customer.getPhone(), saved.total(), productIds));
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