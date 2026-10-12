package org.example.shop.catelog;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponse(Long id, String name, String description, BigDecimal price,
                              int stock, boolean featured, List<String> categories) {

    static ProductResponse from(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getStock(),
                p.isFeatured(),
                p.getCategories().stream().map(Category::getName).sorted().toList());
    }
}
