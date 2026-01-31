package com.validator.sanitizers;

/**
 * Functional interface for defining sanitization rules.
 */
@FunctionalInterface
public interface SanitizerRule {
    /**
     * Sanitizes a string value.
     * 
     * @param value the value to sanitize
     * @return the sanitized value
     */
    String sanitize(String value);
}
