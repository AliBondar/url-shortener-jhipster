package com.bondar.urlshortener.web.rest.errors;

public class ShortCodeNotFoundException extends RuntimeException {

    public ShortCodeNotFoundException(String shortCode) {
        super("Short code not found: " + shortCode);
    }
}
