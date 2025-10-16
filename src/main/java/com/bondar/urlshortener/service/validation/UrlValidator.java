package com.bondar.urlshortener.service.validation;

import java.net.URI;
import org.springframework.stereotype.Component;

@Component
public class UrlValidator {

    public void validateUrl(String url) {
        try {
            URI uri = new URI(url);
            if (!("http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme()))) {
                throw new IllegalArgumentException("Only HTTP and HTTPS URLs are allowed");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid URL format");
        }
    }
}
