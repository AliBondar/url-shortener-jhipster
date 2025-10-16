package com.bondar.urlshortener.service.dto;

import jakarta.validation.constraints.NotBlank;

public class ShortenRequestDTO {

    @NotBlank
    private String originalUrl;

    public ShortenRequestDTO() {}

    public ShortenRequestDTO(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public @NotBlank String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(@NotBlank String originalUrl) {
        this.originalUrl = originalUrl;
    }
}
