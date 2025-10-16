package com.bondar.urlshortener.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.bondar.urlshortener.domain.ShortUrl} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShortUrlDTO implements Serializable {

    private Long id;

    private String originalUrl;

    private String shortCode;

    private LocalDate createdAt;

    private LocalDate expiryAt;

    private Boolean active;

    private Integer accessCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getExpiryAt() {
        return expiryAt;
    }

    public void setExpiryAt(LocalDate expiryAt) {
        this.expiryAt = expiryAt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(Integer accessCount) {
        this.accessCount = accessCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShortUrlDTO)) {
            return false;
        }

        ShortUrlDTO shortUrlDTO = (ShortUrlDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, shortUrlDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShortUrlDTO{" +
            "id=" + getId() +
            ", originalUrl='" + getOriginalUrl() + "'" +
            ", shortCode='" + getShortCode() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", expiryAt='" + getExpiryAt() + "'" +
            ", active='" + getActive() + "'" +
            ", accessCount=" + getAccessCount() +
            "}";
    }
}
