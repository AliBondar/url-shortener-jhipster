package com.bondar.urlshortener.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bondar.urlshortener.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShortUrlDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShortUrlDTO.class);
        ShortUrlDTO shortUrlDTO1 = new ShortUrlDTO();
        shortUrlDTO1.setId(1L);
        ShortUrlDTO shortUrlDTO2 = new ShortUrlDTO();
        assertThat(shortUrlDTO1).isNotEqualTo(shortUrlDTO2);
        shortUrlDTO2.setId(shortUrlDTO1.getId());
        assertThat(shortUrlDTO1).isEqualTo(shortUrlDTO2);
        shortUrlDTO2.setId(2L);
        assertThat(shortUrlDTO1).isNotEqualTo(shortUrlDTO2);
        shortUrlDTO1.setId(null);
        assertThat(shortUrlDTO1).isNotEqualTo(shortUrlDTO2);
    }
}
