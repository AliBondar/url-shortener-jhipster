package com.bondar.urlshortener.service;

import com.bondar.urlshortener.domain.ShortUrl;
import com.bondar.urlshortener.repository.ShortUrlRepository;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UrlCleanupScheduler {

    private static final Logger log = LoggerFactory.getLogger(UrlCleanupScheduler.class);

    private final ShortUrlRepository shortUrlRepository;

    public UrlCleanupScheduler(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    @Scheduled(cron = "${app.cleanup.cron}")
    public void cleanupExpiredUrls() {
        LocalDate now = LocalDate.now();
        List<ShortUrl> expiredUrls = shortUrlRepository.findByActiveTrueAndExpiryAtBefore(now);

        if (expiredUrls.isEmpty()) {
            log.info("No expired URLs found at {}", now);
        }

        expiredUrls.forEach(url -> url.setActive(false));
        shortUrlRepository.saveAll(expiredUrls);

        log.info("Deactivated {} expired URLs at {}", expiredUrls.size(), now);
    }
}
