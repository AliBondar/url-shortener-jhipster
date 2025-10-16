package com.bondar.urlshortener.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ShortUrlTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ShortUrl getShortUrlSample1() {
        return new ShortUrl().id(1L).originalUrl("originalUrl1").shortCode("shortCode1").accessCount(1);
    }

    public static ShortUrl getShortUrlSample2() {
        return new ShortUrl().id(2L).originalUrl("originalUrl2").shortCode("shortCode2").accessCount(2);
    }

    public static ShortUrl getShortUrlRandomSampleGenerator() {
        return new ShortUrl()
            .id(longCount.incrementAndGet())
            .originalUrl(UUID.randomUUID().toString())
            .shortCode(UUID.randomUUID().toString())
            .accessCount(intCount.incrementAndGet());
    }
}
