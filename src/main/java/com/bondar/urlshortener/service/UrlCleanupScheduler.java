package com.bondar.urlshortener.service;

import com.bondar.urlshortener.domain.ShortUrl;
import com.bondar.urlshortener.repository.ShortUrlRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlCleanupScheduler {

    private final ShortUrlRepository shortUrlRepository;

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
