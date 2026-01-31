package com.validator.sanitizers;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.owasp.encoder.Encode;

import java.util.regex.Pattern;

/**
 * Collection of built-in sanitization rules.
 */
public class Sanitizers {

    private static final Pattern SQL_KEYWORDS = Pattern.compile(
        "(?i)(SELECT|INSERT|UPDATE|DELETE|DROP|CREATE|ALTER|EXEC|EXECUTE|UNION|SCRIPT|JAVASCRIPT|ONERROR|ONLOAD)",
        Pattern.CASE_INSENSITIVE
    );

    /**
     * Removes leading and trailing whitespace.
     */
    public static SanitizerRule trim() {
        return value -> value == null ? null : value.trim();
    }

    /**
     * Converts to lowercase.
     */
    public static SanitizerRule toLowerCase() {
        return value -> value == null ? null : value.toLowerCase();
    }

    /**
     * Converts to uppercase.
     */
    public static SanitizerRule toUpperCase() {
        return value -> value == null ? null : value.toUpperCase();
    }

    /**
     * Removes all HTML tags.
     */
    public static SanitizerRule removeHtml() {
        return value -> {
            if (value == null) return null;
            return Jsoup.clean(value, Safelist.none());
        };
    }

    /**
     * Escapes HTML entities.
     */
    public static SanitizerRule escapeHtml() {
        return value -> {
            if (value == null) return null;
            return Encode.forHtml(value);
        };
    }

    /**
     * Escapes XSS attack vectors using OWASP encoder.
     */
    public static SanitizerRule escapeXss() {
        return value -> {
            if (value == null) return null;
            return Encode.forHtmlContent(value);
        };
    }

    /**
     * Removes non-printable characters (control characters).
     */
    public static SanitizerRule removeNonPrintable() {
        return value -> {
            if (value == null) return null;
            return value.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
        };
    }

    /**
     * Removes all non-alphanumeric characters.
     */
    public static SanitizerRule removeNonAlphanumeric() {
        return value -> {
            if (value == null) return null;
            return value.replaceAll("[^a-zA-Z0-9]", "");
        };
    }

    /**
     * Removes all non-numeric characters.
     */
    public static SanitizerRule removeNonNumeric() {
        return value -> {
            if (value == null) return null;
            return value.replaceAll("[^0-9]", "");
        };
    }

    /**
     * Removes all non-alphabetic characters.
     */
    public static SanitizerRule removeNonAlpha() {
        return value -> {
            if (value == null) return null;
            return value.replaceAll("[^a-zA-Z]", "");
        };
    }

    /**
     * Replaces multiple whitespace characters with a single space.
     */
    public static SanitizerRule normalizeWhitespace() {
        return value -> {
            if (value == null) return null;
            return value.replaceAll("\\s+", " ").trim();
        };
    }

    /**
     * Truncates to maximum length.
     * 
     * @param maxLength the maximum length
     */
    public static SanitizerRule maxLength(int maxLength) {
        return value -> {
            if (value == null) return null;
            if (value.length() <= maxLength) return value;
            return value.substring(0, maxLength);
        };
    }

    /**
     * Removes text matching a regex pattern.
     * 
     * @param regex the regex pattern to remove
     */
    public static SanitizerRule removePattern(String regex) {
        return value -> {
            if (value == null) return null;
            return value.replaceAll(regex, "");
        };
    }

    /**
     * Replaces text matching a regex pattern.
     * 
     * @param regex the regex pattern to match
     * @param replacement the replacement text
     */
    public static SanitizerRule replacePattern(String regex, String replacement) {
        return value -> {
            if (value == null) return null;
            return value.replaceAll(regex, replacement);
        };
    }

    /**
     * Removes common SQL injection keywords.
     */
    public static SanitizerRule stripSqlKeywords() {
        return value -> {
            if (value == null) return null;
            return SQL_KEYWORDS.matcher(value).replaceAll("");
        };
    }

    /**
     * Keeps only allowed characters.
     * 
     * @param allowedChars string containing allowed characters
     */
    public static SanitizerRule allowOnly(String allowedChars) {
        return value -> {
            if (value == null) return null;
            String regex = "[^" + Pattern.quote(allowedChars) + "]";
            return value.replaceAll(regex, "");
        };
    }

    /**
     * Removes denied characters.
     * 
     * @param deniedChars string containing denied characters
     */
    public static SanitizerRule deny(String deniedChars) {
        return value -> {
            if (value == null) return null;
            String regex = "[" + Pattern.quote(deniedChars) + "]";
            return value.replaceAll(regex, "");
        };
    }
}
