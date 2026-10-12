package org.example.shop.catelog;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank @Size(max = 200) String name,
        @Size(max = 2000) String description,
        @NotNull @DecimalMin("0.01") @Digits(integer = 8, fraction = 2) BigDecimal price,
        @Min(0) int stock,
        boolean featured
) {}

