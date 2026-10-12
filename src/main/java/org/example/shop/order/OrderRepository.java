package org.example.shop.order;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

interface OrderRepository extends JpaRepository<Order, Long> {

    // Order details: order + items + their products in ONE query
    @EntityGraph(attributePaths = {"items", "items.product"})
    Optional<Order> findWithItemsById(Long id);

    // Order history: only the columns the screen needs, computed by the database
    @Query("""
            select new org.example.shop.order.OrderSummary(
                o.id, o.status, o.createdAt, count(i), sum(i.unitPrice * i.quantity))
            from Order o join o.items i
            where o.customer.id = :userId
            group by o.id, o.status, o.createdAt
            order by o.createdAt desc
            """)
    List<OrderSummary> findSummariesByCustomerId(@Param("userId") Long userId);
}
