package org.example.shop.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record PlaceOrderRequest(
        @NotNull Long userId,
        @NotEmpty @Size(max = 50) @Valid List<Line> items
) {

    public record Line(
            @NotNull Long productId,
            @Min(1) @Max(100) int quantity
    ) {}
}
