package org.example.shop.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("dev")
class DevDataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DevDataSeeder.class);
    private final UserRepository users;
    private  final PasswordEncoder encoder;

    DevDataSeeder(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (users.count() > 0) {
            log.info("Demo data already present, skipping seeding");
            return;
        }
        users.saveAll(List.of(
                new User("Rahim Uddin", "rahim@example.com", "+1 (415) 555-0101",
                        encoder.encode("password123"), Role.CUSTOMER),
                new User("Sara Khan", "sara@example.com", null,
                        encoder.encode("password123"), Role.CUSTOMER),
                new User("Shop Admin", "admin@example.com", null,
                        encoder.encode("admin12345"), Role.ADMIN)));
        log.info("Seeded 3 demo users");
    }
}