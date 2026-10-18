package org.example.shop.security;

import org.example.shop.config.ShopProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class LoginRateLimiter {

    private static final Logger log = LoggerFactory.getLogger(LoginRateLimiter.class);
    private final StringRedisTemplate redis;
    private final int maxFailures;
    private final Duration lockout;

    LoginRateLimiter(StringRedisTemplate redis, ShopProperties props) {
        this.redis = redis;
        this.maxFailures = props.security().maxLoginFailures();
        this.lockout = props.security().loginLockout();
    }

    /** Seconds until this email may try again, or 0 if it isn't blocked. */
    public long secondsUntilAllowed(String email) {
        try {
            String value = redis.opsForValue().get(key(email));
            if (value == null || Long.parseLong(value) < maxFailures) {
                return 0;
            }
            Long ttl = redis.getExpire(key(email));
            return (ttl != null && ttl > 0) ? ttl : lockout.toSeconds();
        } catch (DataAccessException e) {
            log.warn("Rate limiter unavailable, allowing the attempt: {}", e.getMessage());
            return 0;                                              // fail open (see below)
        }
    }

    public void recordFailure(String email) {
        try {
            String key = key(email);
            redis.opsForValue().setIfAbsent(key, "0", lockout);    // start the window, with its TTL
            redis.opsForValue().increment(key);                    // atomic INCR, shared by all instances
        } catch (DataAccessException e) {
            log.warn("Could not record login failure: {}", e.getMessage());
        }
    }

    public void reset(String email) {
        try {
            redis.delete(key(email));
        } catch (DataAccessException e) {
            log.warn("Could not reset login failures: {}", e.getMessage());
        }
    }

    private static String key(String email) {
        return "login-failures:" + email.trim().toLowerCase();
    }
}