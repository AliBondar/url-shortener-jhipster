package com.bondar.urlshortener.repository;

import com.bondar.urlshortener.domain.ShortUrl;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ShortUrl entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    Optional<ShortUrl> findByShortCode(String shortCode);

    Optional<ShortUrl> findByOriginalUrl(String originalUrl);

    int deleteByExpiryAtBefore(LocalDate expiryAt);

    List<ShortUrl> findByActiveTrueAndExpiryAtBefore(LocalDate expiryAt);
}
