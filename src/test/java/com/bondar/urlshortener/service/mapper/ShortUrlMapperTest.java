package com.bondar.urlshortener.service.mapper;

import static com.bondar.urlshortener.domain.ShortUrlAsserts.*;
import static com.bondar.urlshortener.domain.ShortUrlTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShortUrlMapperTest {

    private ShortUrlMapper shortUrlMapper;

    @BeforeEach
    void setUp() {
        shortUrlMapper = new ShortUrlMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getShortUrlSample1();
        var actual = shortUrlMapper.toEntity(shortUrlMapper.toDto(expected));
        assertShortUrlAllPropertiesEquals(expected, actual);
    }
}
