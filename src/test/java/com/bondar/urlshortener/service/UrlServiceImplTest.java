package com.bondar.urlshortener.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import com.bondar.urlshortener.config.ExpiryConfig;
import com.bondar.urlshortener.domain.ShortUrl;
import com.bondar.urlshortener.repository.ShortUrlRepository;
import com.bondar.urlshortener.service.dto.ShortenRequestDTO;
import com.bondar.urlshortener.service.dto.ShortenResponseDTO;
import com.bondar.urlshortener.service.impl.ShortUrlServiceImpl;
import com.bondar.urlshortener.service.validation.UrlValidator;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UrlServiceImplTest {

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @Mock
    private ExpiryConfig expiryConfig;

    @Mock
    private UrlValidator urlValidator;

    @InjectMocks
    private ShortUrlServiceImpl shortUrlService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shortenUrl_whenUrlIsValid_createsAndSavesShortUrl() {
        String originalUrl = "https://somecontext.com";

        ShortUrl saved = new ShortUrl();
        saved.setId(1L);
        saved.setOriginalUrl(originalUrl);
        saved.setShortCode("abcd12");

        doNothing().when(urlValidator).validateUrl(originalUrl);
        when(shortUrlRepository.save(any(ShortUrl.class))).thenReturn(saved);

        ShortenResponseDTO result = shortUrlService.shortenUrl(new ShortenRequestDTO(originalUrl));

        assertNotNull(result.getShortUrl());
        verify(shortUrlRepository, times(1)).save(any(ShortUrl.class));
    }

    @Test
    void shortenUrl_whenUrlIsInvalid_throwsIllegalArgumentException() {
        String invalidUrl = "not-a-valid-url";
        ShortenRequestDTO requestDTO = new ShortenRequestDTO(invalidUrl);

        doThrow(new IllegalArgumentException("Invalid URL format")).when(urlValidator).validateUrl(invalidUrl);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> shortUrlService.shortenUrl(requestDTO));

        assertEquals("Invalid URL format", exception.getMessage());

        verify(shortUrlRepository, never()).save(any(ShortUrl.class));
    }

    @Test
    void getOriginalUrl_whenCodeExistsAndIsActive_shouldReturnOriginalUrl() {
        String shortCode = "abc123";
        ShortUrl found = new ShortUrl();
        found.setOriginalUrl("https://root.com");

        when(shortUrlRepository.findByShortCodeAndActiveTrue(shortCode)).thenReturn(Optional.of(found));

        Optional<String> originalOpt = shortUrlService.getOriginalUrl(shortCode);

        assertTrue(originalOpt.isPresent());
        assertEquals("https://root.com", originalOpt.get());
        verify(shortUrlRepository, times(1)).findByShortCodeAndActiveTrue(shortCode);
    }

    @Test
    void getOriginalUrl_whenCodeDoesNotExistOrIsInactive_shouldReturnEmpty() {
        String shortUrl = "somethingDoesNotExistOrIsInactive";

        when(shortUrlRepository.findByShortCodeAndActiveTrue(shortUrl)).thenReturn(Optional.empty());

        Optional<String> originalUrl = shortUrlService.getOriginalUrl(shortUrl);

        assertFalse(originalUrl.isPresent());

        verify(shortUrlRepository, times(1)).findByShortCodeAndActiveTrue(shortUrl);
    }
}
