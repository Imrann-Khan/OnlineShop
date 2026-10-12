package org.example.shop.catelog;

public record CategoryResponse(Long id, String name) {
    static CategoryResponse from(Category c) {
        return new CategoryResponse(
                c.getId(),
                c.getName()
        );
    }
}
