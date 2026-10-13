package org.example.shop.discount;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DiscountConfig {

    @Bean
    DiscountStrategy summer10() {
        return new PercentOffDiscount("SUMMER10", 10);
    }

    @Bean
    DiscountStrategy student20() {
        return new PercentOffDiscount("STUDENT20", 20);
    }
}