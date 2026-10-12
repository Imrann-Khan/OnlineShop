package org.example.shop.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("dev")
class DevDataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DevDataSeeder.class);
    private final UserRepository users;

    DevDataSeeder(UserRepository users) {
        this.users = users;
    }

    @Override
    public void run(String... args) {
        if (users.count() > 0) {
            log.info("Demo data already present, skipping seeding");
            return;
        }
        users.saveAll(List.of(
                new User("Rahim Uddin", "rahim@example.com"),
                new User("Sara Khan", "sara@example.com"),
                new User("Shop Admin", "admin@example.com")));
        log.info("Seeded 3 demo users");
    }
}