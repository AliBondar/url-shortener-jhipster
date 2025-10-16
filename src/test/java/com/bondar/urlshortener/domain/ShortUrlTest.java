package com.bondar.urlshortener.domain;

import static com.bondar.urlshortener.domain.ShortUrlTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bondar.urlshortener.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShortUrlTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShortUrl.class);
        ShortUrl shortUrl1 = getShortUrlSample1();
        ShortUrl shortUrl2 = new ShortUrl();
        assertThat(shortUrl1).isNotEqualTo(shortUrl2);

        shortUrl2.setId(shortUrl1.getId());
        assertThat(shortUrl1).isEqualTo(shortUrl2);

        shortUrl2 = getShortUrlSample2();
        assertThat(shortUrl1).isNotEqualTo(shortUrl2);
    }
}
