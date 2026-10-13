package org.example.shop.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
class StartupLogger {

    private static final Logger log = LoggerFactory.getLogger(StartupLogger.class);
    private final Environment environment;
    private final ShopProperties props;

    StartupLogger(Environment environment, ShopProperties props) {
        this.environment = environment;
        this.props = props;
    }

    @EventListener(ApplicationReadyEvent.class)
    void onReady() {
        String[] active = environment.getActiveProfiles();
        String[] profiles = active.length > 0 ? active : environment.getDefaultProfiles();
        log.info("Shop API ready | profiles={} | notifications={} | page size={}/{}",
                Arrays.toString(profiles),
                props.notifications().channels(),
                props.pagination().defaultSize(),
                props.pagination().maxSize());
    }
}