package com.bondar.urlshortener.service.dto;

public class ShortenResponseDTO {

    private String shortUrl;

    public ShortenResponseDTO() {}

    public ShortenResponseDTO(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}
