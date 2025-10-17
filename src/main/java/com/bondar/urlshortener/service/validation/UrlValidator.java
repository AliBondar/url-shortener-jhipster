package com.bondar.urlshortener.service.validation;

import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.stereotype.Component;

@Component
public class UrlValidator {

    public void validateUrl(String url) throws IllegalArgumentException {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }

        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL format: " + e.getMessage(), e);
        }

        String scheme = uri.getScheme();
        if (!("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme))) {
            IllegalArgumentException ex = new IllegalArgumentException("Only HTTP and HTTPS URLs are allowed");
            System.out.println(ex.getMessage());
            throw ex;
        }

        if (uri.getHost() == null) {
            throw new IllegalArgumentException("URL must include a valid host");
        }
    }
}
