package org.example.shop.order;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("orderSecurity")
public class OrderSecurity {

    private final OrderRepository orders;

    public OrderSecurity(OrderRepository orders) {
        this.orders = orders;
    }

    public boolean isOwner(Long orderId, Authentication authentication) {
        return orders.findCustomerIdById(orderId)
                .map(customerId -> customerId.toString().equals(authentication.getName()))
                .orElse(false);
    }
}
