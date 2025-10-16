package com.bondar.urlshortener.repository;

import com.bondar.urlshortener.domain.ShortUrl;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ShortUrl entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {}
