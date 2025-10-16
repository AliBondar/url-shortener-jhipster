package com.bondar.urlshortener.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExpiryConfig {

    @Value("${app.retention-days}")
    int retentionDays;

    public int getRetentionDays() {
        return retentionDays;
    }
}
