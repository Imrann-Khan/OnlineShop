package org.example.shop.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@ConfigurationProperties(prefix = "shop")
@Validated
public record ShopProperties(
        @Valid @NotNull Pagination pagination,
        @Valid @NotNull Notifications notifications) {

    public record Pagination(
            @Min(1) @Max(100) int defaultSize,
            @Min(1) @Max(500) int maxSize) {}

    public record Notifications(
            @NotEmpty List<@NotBlank String> channels,
            String smsApiKey
    ) {}
}
