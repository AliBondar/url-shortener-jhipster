package com.bondar.urlshortener.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ExpiryConfig {

    @Value("${app.retention-days}")
    int retentionDays;
}
