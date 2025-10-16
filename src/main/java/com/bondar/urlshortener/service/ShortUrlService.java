package com.bondar.urlshortener.service;

import com.bondar.urlshortener.service.dto.ShortUrlDTO;
import com.bondar.urlshortener.service.dto.ShortenRequestDTO;
import com.bondar.urlshortener.service.dto.ShortenResponseDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.bondar.urlshortener.domain.ShortUrl}.
 */
public interface ShortUrlService {
    /**
     * Save a shortUrl.
     *
     * @param shortUrlDTO the entity to save.
     * @return the persisted entity.
     */
    ShortUrlDTO save(ShortUrlDTO shortUrlDTO);

    /**
     * Updates a shortUrl.
     *
     * @param shortUrlDTO the entity to update.
     * @return the persisted entity.
     */
    ShortUrlDTO update(ShortUrlDTO shortUrlDTO);

    /**
     * Partially updates a shortUrl.
     *
     * @param shortUrlDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ShortUrlDTO> partialUpdate(ShortUrlDTO shortUrlDTO);

    /**
     * Get all the shortUrls.
     *
     * @return the list of entities.
     */
    List<ShortUrlDTO> findAll();

    /**
     * Get the "id" shortUrl.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ShortUrlDTO> findOne(Long id);

    /**
     * Delete the "id" shortUrl.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    ShortenResponseDTO shortenUrl(ShortenRequestDTO dto);

    Optional<String> getOriginalUrl(String shortCode);
}
