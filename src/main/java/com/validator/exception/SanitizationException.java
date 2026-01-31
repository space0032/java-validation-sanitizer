package com.validator.exception;

/**
 * Exception thrown when sanitization fails.
 */
public class SanitizationException extends RuntimeException {
    public SanitizationException(String message) {
        super(message);
    }

    public SanitizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
