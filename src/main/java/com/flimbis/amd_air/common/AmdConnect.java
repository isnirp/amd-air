package com.flimbis.amd_air.common;

import com.amadeus.Amadeus;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AmdConnect {
    private static final Logger logger = LoggerFactory.getLogger(AmdConnect.class);

    @Value("${amadeus.client.id:}")
    private String clientId;

    @Value("${amadeus.client.secret:}")
    private String clientSecret;

    private volatile Amadeus amadeus;
    private final Object lock = new Object();

    @PostConstruct
    public void init() {
        if (clientId == null || clientId.trim().isEmpty()) {
            logger.error("Amadeus client ID is not configured");
            throw new IllegalStateException("Amadeus client ID must be configured");
        }

        if (clientSecret == null || clientSecret.trim().isEmpty()) {
            logger.error("Amadeus client secret is not configured");
            throw new IllegalStateException("Amadeus client secret must be configured");
        }

        try {
            this.amadeus = Amadeus
                    .builder(clientId.trim(), clientSecret.trim())
                    .build();
            logger.info("Amadeus client initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize Amadeus client", e);
            throw new IllegalStateException("Failed to initialize Amadeus client", e);
        }
    }

    public Amadeus getAmadeus() {
        if (amadeus == null) {
            synchronized (lock) {
                if (amadeus == null) {
                    throw new IllegalStateException("Amadeus client is not initialized");
                }
            }
        }
        return amadeus;
    }

    @PreDestroy
    public void destroy() {
        if (amadeus != null) {
            logger.info("Cleaning up Amadeus client resources");
            amadeus = null;
        }
    }
}
