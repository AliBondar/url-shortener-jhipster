package com.bondar.urlshortener.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.bondar.urlshortener.domain.ShortUrl;
import com.bondar.urlshortener.repository.ShortUrlRepository;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class UrlCleanupSchedulerTest {

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @InjectMocks
    private UrlCleanupScheduler urlCleanupScheduler;

    @BeforeEach
    void setUp() {
        shortUrlRepository = mock(ShortUrlRepository.class);
        urlCleanupScheduler = new UrlCleanupScheduler(shortUrlRepository);
    }

    @Test
    void cleanupExpiredUrls_shouldDeactivateExpiredUrls() {
        LocalDate today = LocalDate.now();
        ShortUrl url1 = new ShortUrl();
        url1.setActive(true);
        url1.setExpiryAt(today.minusDays(1));

        ShortUrl url2 = new ShortUrl();
        url2.setActive(true);
        url2.setExpiryAt(today.minusDays(5));

        List<ShortUrl> expiredUrls = List.of(url1, url2);

        when(shortUrlRepository.findByActiveTrueAndExpiryAtBefore(today)).thenReturn(expiredUrls);

        urlCleanupScheduler.cleanupExpiredUrls();

        assertFalse(url1.getActive());
        assertFalse(url2.getActive());

        ArgumentCaptor<List<ShortUrl>> captor = ArgumentCaptor.forClass(List.class);
        verify(shortUrlRepository, times(1)).saveAll(captor.capture());

        List<ShortUrl> savedUrls = captor.getValue();
        assertEquals(2, savedUrls.size());
        assertTrue(savedUrls.stream().allMatch(u -> !u.getActive()));
    }

    @Test
    void cleanupExpiredUrls_shouldDoNothingIfNoExpiredUrls() {
        LocalDate today = LocalDate.now();
        when(shortUrlRepository.findByActiveTrueAndExpiryAtBefore(today)).thenReturn(List.of());

        urlCleanupScheduler.cleanupExpiredUrls();

        verify(shortUrlRepository).saveAll(Collections.emptyList());
    }
}
