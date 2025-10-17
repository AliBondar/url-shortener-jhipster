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
    void testValidateUrl_validHttpUrl_doesNotThrow() {
        assertDoesNotThrow(() -> urlValidator.validateUrl("http://example.com"));
    }

    @Test
    void testValidateUrl_validHttpsUrl_doesNotThrow() {
        assertDoesNotThrow(() -> urlValidator.validateUrl("https://www.example.com/search?q=jhipster"));
    }
}
