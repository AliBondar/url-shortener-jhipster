package com.bondar.urlshortener.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ShortUrl.
 */
@Entity
@Table(name = "short_url")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShortUrl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "original_url")
    private String originalUrl;

    @Column(name = "short_code")
    private String shortCode;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "expiry_at")
    private LocalDate expiryAt;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "access_count")
    private Integer accessCount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ShortUrl id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return this.originalUrl;
    }

    public ShortUrl originalUrl(String originalUrl) {
        this.setOriginalUrl(originalUrl);
        return this;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortCode() {
        return this.shortCode;
    }

    public ShortUrl shortCode(String shortCode) {
        this.setShortCode(shortCode);
        return this;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public ShortUrl createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getExpiryAt() {
        return this.expiryAt;
    }

    public ShortUrl expiryAt(LocalDate expiryAt) {
        this.setExpiryAt(expiryAt);
        return this;
    }

    public void setExpiryAt(LocalDate expiryAt) {
        this.expiryAt = expiryAt;
    }

    public Boolean getActive() {
        return this.active;
    }

    public ShortUrl active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getAccessCount() {
        return this.accessCount;
    }

    public ShortUrl accessCount(Integer accessCount) {
        this.setAccessCount(accessCount);
        return this;
    }

    public void setAccessCount(Integer accessCount) {
        this.accessCount = accessCount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShortUrl)) {
            return false;
        }
        return getId() != null && getId().equals(((ShortUrl) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShortUrl{" +
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
