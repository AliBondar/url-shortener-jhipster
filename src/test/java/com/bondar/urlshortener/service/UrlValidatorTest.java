package com.bondar.urlshortener.service;

import static org.junit.jupiter.api.Assertions.*;

import com.bondar.urlshortener.service.validation.UrlValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UrlValidatorTest {

    @Mock
    private UrlValidator urlValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateUrl_validUrl() {
        String validUrl = "https://example.com";

        try {
            urlValidator.validateUrl(validUrl);
        } catch (IllegalArgumentException e) {
            fail("Expected valid URL not to throw an exception, but got: " + e.getMessage());
        }
    }

    @Test
    void testValidateUrl_invalidUrl_throws() {
        String badUrl = "abc://example.com";

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> urlValidator.validateUrl(badUrl));

        assertEquals("Only HTTP and HTTPS URLs are allowed", ex.getMessage());
    }
}
