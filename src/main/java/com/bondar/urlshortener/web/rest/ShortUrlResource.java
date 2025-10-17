package com.bondar.urlshortener.web.rest;

import com.bondar.urlshortener.repository.ShortUrlRepository;
import com.bondar.urlshortener.service.ShortUrlService;
import com.bondar.urlshortener.service.dto.ShortUrlDTO;
import com.bondar.urlshortener.service.dto.ShortenRequestDTO;
import com.bondar.urlshortener.service.dto.ShortenResponseDTO;
import com.bondar.urlshortener.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bondar.urlshortener.domain.ShortUrl}.
 */
@RestController
@RequestMapping("/api/short-urls")
public class ShortUrlResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShortUrlResource.class);

    private static final String ENTITY_NAME = "shortUrl";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShortUrlService shortUrlService;

    private final ShortUrlRepository shortUrlRepository;

    public ShortUrlResource(ShortUrlService shortUrlService, ShortUrlRepository shortUrlRepository) {
        this.shortUrlService = shortUrlService;
        this.shortUrlRepository = shortUrlRepository;
    }

    @PostMapping("/shorten")
    public ResponseEntity<ShortenResponseDTO> shorten(@Valid @RequestBody ShortenRequestDTO request) {
        ShortenResponseDTO response = shortUrlService.shortenUrl(request);
        return ResponseEntity.created(URI.create(response.getShortUrl())).body(response);
    }

    @GetMapping("/redirect/{shortCode}")
    public RedirectView redirect(@PathVariable String shortCode) {
        return shortUrlService.getOriginalUrl(shortCode).map(url -> new RedirectView(url)).orElseGet(() -> new RedirectView("/404"));
    }

    @GetMapping("/get-short-url/{shortCode}")
    public ResponseEntity<String> getOriginalUrl(@PathVariable String shortCode) {
        return shortUrlService.getOriginalUrl(shortCode).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * {@code POST  /short-urls} : Create a new shortUrl.
     *
     * @param shortUrlDTO the shortUrlDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shortUrlDTO, or with status {@code 400 (Bad Request)} if the shortUrl has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShortUrlDTO> createShortUrl(@RequestBody ShortUrlDTO shortUrlDTO) throws URISyntaxException {
        LOG.debug("REST request to save ShortUrl : {}", shortUrlDTO);
        if (shortUrlDTO.getId() != null) {
            throw new BadRequestAlertException("A new shortUrl cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shortUrlDTO = shortUrlService.save(shortUrlDTO);
        return ResponseEntity.created(new URI("/api/short-urls/" + shortUrlDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, shortUrlDTO.getId().toString()))
            .body(shortUrlDTO);
    }

    /**
     * {@code PUT  /short-urls/:id} : Updates an existing shortUrl.
     *
     * @param id the id of the shortUrlDTO to save.
     * @param shortUrlDTO the shortUrlDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shortUrlDTO,
     * or with status {@code 400 (Bad Request)} if the shortUrlDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shortUrlDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShortUrlDTO> updateShortUrl(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ShortUrlDTO shortUrlDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ShortUrl : {}, {}", id, shortUrlDTO);
        if (shortUrlDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shortUrlDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shortUrlRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shortUrlDTO = shortUrlService.update(shortUrlDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, shortUrlDTO.getId().toString()))
            .body(shortUrlDTO);
    }

    /**
     * {@code PATCH  /short-urls/:id} : Partial updates given fields of an existing shortUrl, field will ignore if it is null
     *
     * @param id the id of the shortUrlDTO to save.
     * @param shortUrlDTO the shortUrlDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shortUrlDTO,
     * or with status {@code 400 (Bad Request)} if the shortUrlDTO is not valid,
     * or with status {@code 404 (Not Found)} if the shortUrlDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the shortUrlDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShortUrlDTO> partialUpdateShortUrl(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ShortUrlDTO shortUrlDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ShortUrl partially : {}, {}", id, shortUrlDTO);
        if (shortUrlDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shortUrlDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shortUrlRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShortUrlDTO> result = shortUrlService.partialUpdate(shortUrlDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, shortUrlDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /short-urls} : get all the shortUrls.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shortUrls in body.
     */
    @GetMapping("")
    public List<ShortUrlDTO> getAllShortUrls() {
        LOG.debug("REST request to get all ShortUrls");
        return shortUrlService.findAll();
    }

    /**
     * {@code GET  /short-urls/:id} : get the "id" shortUrl.
     *
     * @param id the id of the shortUrlDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shortUrlDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShortUrlDTO> getShortUrl(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ShortUrl : {}", id);
        Optional<ShortUrlDTO> shortUrlDTO = shortUrlService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shortUrlDTO);
    }

    /**
     * {@code DELETE  /short-urls/:id} : delete the "id" shortUrl.
     *
     * @param id the id of the shortUrlDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShortUrl(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ShortUrl : {}", id);
        shortUrlService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/version")
    public ResponseEntity<Map<String, String>> getVersion() {
        Map<String, String> versionInfo = new HashMap<>();
        versionInfo.put("version", "1.0.0");
        versionInfo.put("description", "URL Shortener Service(Built by JHipster)");
        versionInfo.put("author", "Ali Bondar");
        versionInfo.put("date", LocalDateTime.now().toString());
        return ResponseEntity.ok(versionInfo);
    }
}
