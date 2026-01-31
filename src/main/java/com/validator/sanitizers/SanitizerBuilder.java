package com.validator.sanitizers;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for chaining sanitization operations.
 */
public class SanitizerBuilder {
    private String value;
    private final List<SanitizerRule> rules;

    public SanitizerBuilder(String value) {
        this.value = value;
        this.rules = new ArrayList<>();
    }

    /**
     * Adds a sanitization rule to the chain.
     * 
     * @param rule the sanitization rule
     * @return this builder for chaining
     */
    public SanitizerBuilder apply(SanitizerRule rule) {
        rules.add(rule);
        return this;
    }

    /**
     * Applies all sanitization rules and returns the result.
     * 
     * @return the sanitized value
     */
    public String sanitize() {
        String result = value;
        for (SanitizerRule rule : rules) {
            if (result != null) {
                result = rule.sanitize(result);
            }
        }
        return result;
    }

    // Convenience methods that delegate to Sanitizers
    public SanitizerBuilder trim() {
        return apply(Sanitizers.trim());
    }

    public SanitizerBuilder toLowerCase() {
        return apply(Sanitizers.toLowerCase());
    }

    public SanitizerBuilder toUpperCase() {
        return apply(Sanitizers.toUpperCase());
    }

    public SanitizerBuilder removeHtml() {
        return apply(Sanitizers.removeHtml());
    }

    public SanitizerBuilder escapeHtml() {
        return apply(Sanitizers.escapeHtml());
    }

    public SanitizerBuilder escapeXss() {
        return apply(Sanitizers.escapeXss());
    }

    public SanitizerBuilder removeNonPrintable() {
        return apply(Sanitizers.removeNonPrintable());
    }

    public SanitizerBuilder removeNonAlphanumeric() {
        return apply(Sanitizers.removeNonAlphanumeric());
    }

    public SanitizerBuilder removeNonNumeric() {
        return apply(Sanitizers.removeNonNumeric());
    }

    public SanitizerBuilder removeNonAlpha() {
        return apply(Sanitizers.removeNonAlpha());
    }

    public SanitizerBuilder normalizeWhitespace() {
        return apply(Sanitizers.normalizeWhitespace());
    }

    public SanitizerBuilder maxLength(int maxLength) {
        return apply(Sanitizers.maxLength(maxLength));
    }

    public SanitizerBuilder removePattern(String regex) {
        return apply(Sanitizers.removePattern(regex));
    }

    public SanitizerBuilder replacePattern(String regex, String replacement) {
        return apply(Sanitizers.replacePattern(regex, replacement));
    }

    public SanitizerBuilder stripSqlKeywords() {
        return apply(Sanitizers.stripSqlKeywords());
    }

    public SanitizerBuilder allowOnly(String allowedChars) {
        return apply(Sanitizers.allowOnly(allowedChars));
    }

    public SanitizerBuilder deny(String deniedChars) {
        return apply(Sanitizers.deny(deniedChars));
    }
}
