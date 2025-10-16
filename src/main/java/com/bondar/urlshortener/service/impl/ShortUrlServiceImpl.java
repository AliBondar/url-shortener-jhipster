package com.bondar.urlshortener.service.impl;

import com.bondar.urlshortener.domain.ShortUrl;
import com.bondar.urlshortener.repository.ShortUrlRepository;
import com.bondar.urlshortener.service.ShortUrlService;
import com.bondar.urlshortener.service.dto.ShortUrlDTO;
import com.bondar.urlshortener.service.mapper.ShortUrlMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bondar.urlshortener.domain.ShortUrl}.
 */
@Service
@Transactional
public class ShortUrlServiceImpl implements ShortUrlService {

    private static final Logger LOG = LoggerFactory.getLogger(ShortUrlServiceImpl.class);

    private final ShortUrlRepository shortUrlRepository;

    private final ShortUrlMapper shortUrlMapper;

    public ShortUrlServiceImpl(ShortUrlRepository shortUrlRepository, ShortUrlMapper shortUrlMapper) {
        this.shortUrlRepository = shortUrlRepository;
        this.shortUrlMapper = shortUrlMapper;
    }

    @Override
    public ShortUrlDTO save(ShortUrlDTO shortUrlDTO) {
        LOG.debug("Request to save ShortUrl : {}", shortUrlDTO);
        ShortUrl shortUrl = shortUrlMapper.toEntity(shortUrlDTO);
        shortUrl = shortUrlRepository.save(shortUrl);
        return shortUrlMapper.toDto(shortUrl);
    }

    @Override
    public ShortUrlDTO update(ShortUrlDTO shortUrlDTO) {
        LOG.debug("Request to update ShortUrl : {}", shortUrlDTO);
        ShortUrl shortUrl = shortUrlMapper.toEntity(shortUrlDTO);
        shortUrl = shortUrlRepository.save(shortUrl);
        return shortUrlMapper.toDto(shortUrl);
    }

    @Override
    public Optional<ShortUrlDTO> partialUpdate(ShortUrlDTO shortUrlDTO) {
        LOG.debug("Request to partially update ShortUrl : {}", shortUrlDTO);

        return shortUrlRepository
            .findById(shortUrlDTO.getId())
            .map(existingShortUrl -> {
                shortUrlMapper.partialUpdate(existingShortUrl, shortUrlDTO);

                return existingShortUrl;
            })
            .map(shortUrlRepository::save)
            .map(shortUrlMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShortUrlDTO> findAll() {
        LOG.debug("Request to get all ShortUrls");
        return shortUrlRepository.findAll().stream().map(shortUrlMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShortUrlDTO> findOne(Long id) {
        LOG.debug("Request to get ShortUrl : {}", id);
        return shortUrlRepository.findById(id).map(shortUrlMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ShortUrl : {}", id);
        shortUrlRepository.deleteById(id);
    }
}
