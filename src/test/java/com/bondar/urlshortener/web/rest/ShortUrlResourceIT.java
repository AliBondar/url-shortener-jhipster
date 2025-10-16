package com.bondar.urlshortener.web.rest;

import static com.bondar.urlshortener.domain.ShortUrlAsserts.*;
import static com.bondar.urlshortener.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bondar.urlshortener.IntegrationTest;
import com.bondar.urlshortener.domain.ShortUrl;
import com.bondar.urlshortener.repository.ShortUrlRepository;
import com.bondar.urlshortener.service.dto.ShortUrlDTO;
import com.bondar.urlshortener.service.mapper.ShortUrlMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ShortUrlResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShortUrlResourceIT {

    private static final String DEFAULT_ORIGINAL_URL = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINAL_URL = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_EXPIRY_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRY_AT = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Integer DEFAULT_ACCESS_COUNT = 1;
    private static final Integer UPDATED_ACCESS_COUNT = 2;

    private static final String ENTITY_API_URL = "/api/short-urls";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @Autowired
    private ShortUrlMapper shortUrlMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShortUrlMockMvc;

    private ShortUrl shortUrl;

    private ShortUrl insertedShortUrl;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShortUrl createEntity() {
        return new ShortUrl()
            .originalUrl(DEFAULT_ORIGINAL_URL)
            .shortCode(DEFAULT_SHORT_CODE)
            .createdAt(DEFAULT_CREATED_AT)
            .expiryAt(DEFAULT_EXPIRY_AT)
            .active(DEFAULT_ACTIVE)
            .accessCount(DEFAULT_ACCESS_COUNT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShortUrl createUpdatedEntity() {
        return new ShortUrl()
            .originalUrl(UPDATED_ORIGINAL_URL)
            .shortCode(UPDATED_SHORT_CODE)
            .createdAt(UPDATED_CREATED_AT)
            .expiryAt(UPDATED_EXPIRY_AT)
            .active(UPDATED_ACTIVE)
            .accessCount(UPDATED_ACCESS_COUNT);
    }

    @BeforeEach
    void initTest() {
        shortUrl = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedShortUrl != null) {
            shortUrlRepository.delete(insertedShortUrl);
            insertedShortUrl = null;
        }
    }

    @Test
    @Transactional
    void createShortUrl() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ShortUrl
        ShortUrlDTO shortUrlDTO = shortUrlMapper.toDto(shortUrl);
        var returnedShortUrlDTO = om.readValue(
            restShortUrlMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shortUrlDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShortUrlDTO.class
        );

        // Validate the ShortUrl in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedShortUrl = shortUrlMapper.toEntity(returnedShortUrlDTO);
        assertShortUrlUpdatableFieldsEquals(returnedShortUrl, getPersistedShortUrl(returnedShortUrl));

        insertedShortUrl = returnedShortUrl;
    }

    @Test
    @Transactional
    void createShortUrlWithExistingId() throws Exception {
        // Create the ShortUrl with an existing ID
        shortUrl.setId(1L);
        ShortUrlDTO shortUrlDTO = shortUrlMapper.toDto(shortUrl);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShortUrlMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shortUrlDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShortUrl in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllShortUrls() throws Exception {
        // Initialize the database
        insertedShortUrl = shortUrlRepository.saveAndFlush(shortUrl);

        // Get all the shortUrlList
        restShortUrlMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shortUrl.getId().intValue())))
            .andExpect(jsonPath("$.[*].originalUrl").value(hasItem(DEFAULT_ORIGINAL_URL)))
            .andExpect(jsonPath("$.[*].shortCode").value(hasItem(DEFAULT_SHORT_CODE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].expiryAt").value(hasItem(DEFAULT_EXPIRY_AT.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)))
            .andExpect(jsonPath("$.[*].accessCount").value(hasItem(DEFAULT_ACCESS_COUNT)));
    }

    @Test
    @Transactional
    void getShortUrl() throws Exception {
        // Initialize the database
        insertedShortUrl = shortUrlRepository.saveAndFlush(shortUrl);

        // Get the shortUrl
        restShortUrlMockMvc
            .perform(get(ENTITY_API_URL_ID, shortUrl.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shortUrl.getId().intValue()))
            .andExpect(jsonPath("$.originalUrl").value(DEFAULT_ORIGINAL_URL))
            .andExpect(jsonPath("$.shortCode").value(DEFAULT_SHORT_CODE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.expiryAt").value(DEFAULT_EXPIRY_AT.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE))
            .andExpect(jsonPath("$.accessCount").value(DEFAULT_ACCESS_COUNT));
    }

    @Test
    @Transactional
    void getNonExistingShortUrl() throws Exception {
        // Get the shortUrl
        restShortUrlMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShortUrl() throws Exception {
        // Initialize the database
        insertedShortUrl = shortUrlRepository.saveAndFlush(shortUrl);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shortUrl
        ShortUrl updatedShortUrl = shortUrlRepository.findById(shortUrl.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShortUrl are not directly saved in db
        em.detach(updatedShortUrl);
        updatedShortUrl
            .originalUrl(UPDATED_ORIGINAL_URL)
            .shortCode(UPDATED_SHORT_CODE)
            .createdAt(UPDATED_CREATED_AT)
            .expiryAt(UPDATED_EXPIRY_AT)
            .active(UPDATED_ACTIVE)
            .accessCount(UPDATED_ACCESS_COUNT);
        ShortUrlDTO shortUrlDTO = shortUrlMapper.toDto(updatedShortUrl);

        restShortUrlMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shortUrlDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shortUrlDTO))
            )
            .andExpect(status().isOk());

        // Validate the ShortUrl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShortUrlToMatchAllProperties(updatedShortUrl);
    }

    @Test
    @Transactional
    void putNonExistingShortUrl() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shortUrl.setId(longCount.incrementAndGet());

        // Create the ShortUrl
        ShortUrlDTO shortUrlDTO = shortUrlMapper.toDto(shortUrl);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShortUrlMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shortUrlDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shortUrlDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShortUrl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShortUrl() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shortUrl.setId(longCount.incrementAndGet());

        // Create the ShortUrl
        ShortUrlDTO shortUrlDTO = shortUrlMapper.toDto(shortUrl);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShortUrlMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shortUrlDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShortUrl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShortUrl() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shortUrl.setId(longCount.incrementAndGet());

        // Create the ShortUrl
        ShortUrlDTO shortUrlDTO = shortUrlMapper.toDto(shortUrl);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShortUrlMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shortUrlDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShortUrl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShortUrlWithPatch() throws Exception {
        // Initialize the database
        insertedShortUrl = shortUrlRepository.saveAndFlush(shortUrl);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shortUrl using partial update
        ShortUrl partialUpdatedShortUrl = new ShortUrl();
        partialUpdatedShortUrl.setId(shortUrl.getId());

        partialUpdatedShortUrl.expiryAt(UPDATED_EXPIRY_AT).active(UPDATED_ACTIVE);

        restShortUrlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShortUrl.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShortUrl))
            )
            .andExpect(status().isOk());

        // Validate the ShortUrl in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShortUrlUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedShortUrl, shortUrl), getPersistedShortUrl(shortUrl));
    }

    @Test
    @Transactional
    void fullUpdateShortUrlWithPatch() throws Exception {
        // Initialize the database
        insertedShortUrl = shortUrlRepository.saveAndFlush(shortUrl);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shortUrl using partial update
        ShortUrl partialUpdatedShortUrl = new ShortUrl();
        partialUpdatedShortUrl.setId(shortUrl.getId());

        partialUpdatedShortUrl
            .originalUrl(UPDATED_ORIGINAL_URL)
            .shortCode(UPDATED_SHORT_CODE)
            .createdAt(UPDATED_CREATED_AT)
            .expiryAt(UPDATED_EXPIRY_AT)
            .active(UPDATED_ACTIVE)
            .accessCount(UPDATED_ACCESS_COUNT);

        restShortUrlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShortUrl.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShortUrl))
            )
            .andExpect(status().isOk());

        // Validate the ShortUrl in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShortUrlUpdatableFieldsEquals(partialUpdatedShortUrl, getPersistedShortUrl(partialUpdatedShortUrl));
    }

    @Test
    @Transactional
    void patchNonExistingShortUrl() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shortUrl.setId(longCount.incrementAndGet());

        // Create the ShortUrl
        ShortUrlDTO shortUrlDTO = shortUrlMapper.toDto(shortUrl);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShortUrlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shortUrlDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shortUrlDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShortUrl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShortUrl() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shortUrl.setId(longCount.incrementAndGet());

        // Create the ShortUrl
        ShortUrlDTO shortUrlDTO = shortUrlMapper.toDto(shortUrl);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShortUrlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shortUrlDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShortUrl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShortUrl() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shortUrl.setId(longCount.incrementAndGet());

        // Create the ShortUrl
        ShortUrlDTO shortUrlDTO = shortUrlMapper.toDto(shortUrl);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShortUrlMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shortUrlDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShortUrl in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShortUrl() throws Exception {
        // Initialize the database
        insertedShortUrl = shortUrlRepository.saveAndFlush(shortUrl);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shortUrl
        restShortUrlMockMvc
            .perform(delete(ENTITY_API_URL_ID, shortUrl.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shortUrlRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ShortUrl getPersistedShortUrl(ShortUrl shortUrl) {
        return shortUrlRepository.findById(shortUrl.getId()).orElseThrow();
    }

    protected void assertPersistedShortUrlToMatchAllProperties(ShortUrl expectedShortUrl) {
        assertShortUrlAllPropertiesEquals(expectedShortUrl, getPersistedShortUrl(expectedShortUrl));
    }

    protected void assertPersistedShortUrlToMatchUpdatableProperties(ShortUrl expectedShortUrl) {
        assertShortUrlAllUpdatablePropertiesEquals(expectedShortUrl, getPersistedShortUrl(expectedShortUrl));
    }
}
