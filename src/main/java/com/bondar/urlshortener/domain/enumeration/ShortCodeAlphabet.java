package com.bondar.urlshortener.domain.enumeration;

public enum ShortCodeAlphabet {
    ALPHA_NUMERIC("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"),
    NUMERIC("0123456789"),
    ALPHABETIC("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");

    private final String chars;

    ShortCodeAlphabet(String chars) {
        this.chars = chars;
    }

    public String getChars() {
        return chars;
    }
}
