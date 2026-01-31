package com.validator.validators;

import com.validator.core.ValidatorRule;
import com.validator.result.ValidationError;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * Collection of built-in validation rules.
 */
public class Validators {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_]+(\\.[A-Za-z0-9+_]+)*@[A-Za-z0-9]([A-Za-z0-9-]*[A-Za-z0-9])?(\\.[A-Za-z0-9]([A-Za-z0-9-]*[A-Za-z0-9])?)*\\.[A-Za-z]{2,}$"
    );

    private static final Pattern URL_PATTERN = Pattern.compile(
        "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$",
        Pattern.CASE_INSENSITIVE
    );

    private static final Pattern UUID_PATTERN = Pattern.compile(
        "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
    );

    private static final Pattern IPV4_PATTERN = Pattern.compile(
        "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
    );

    private static final Pattern IPV6_PATTERN = Pattern.compile(
        "^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$|^::([0-9a-fA-F]{1,4}:){0,6}[0-9a-fA-F]{1,4}$|^([0-9a-fA-F]{1,4}:){1,7}:$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[+]?[(]?[0-9]{1,4}[)]?[-\\s.()0-9]*[0-9]$"
    );

    private static final Pattern ALPHA_PATTERN = Pattern.compile("^[a-zA-Z]+$");
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("^[0-9]+$");

    /**
     * Validates that value is not null.
     */
    public static <T> ValidatorRule<T> notNull() {
        return (fieldName, value) -> {
            if (value == null) {
                return new ValidationError(fieldName, "must not be null", "notNull", value);
            }
            return null;
        };
    }

    /**
     * Validates that string is not empty.
     */
    public static ValidatorRule<String> notEmpty() {
        return (fieldName, value) -> {
            if (value == null || value.isEmpty()) {
                return new ValidationError(fieldName, "must not be empty", "notEmpty", value);
            }
            return null;
        };
    }

    /**
     * Validates that string is not blank (not null, not empty, not just whitespace).
     */
    public static ValidatorRule<String> notBlank() {
        return (fieldName, value) -> {
            if (value == null || value.trim().isEmpty()) {
                return new ValidationError(fieldName, "must not be blank", "notBlank", value);
            }
            return null;
        };
    }

    /**
     * Validates email format.
     */
    public static ValidatorRule<String> isEmail() {
        return (fieldName, value) -> {
            if (value != null && !EMAIL_PATTERN.matcher(value).matches()) {
                return new ValidationError(fieldName, "must be a valid email", "isEmail", value);
            }
            return null;
        };
    }

    /**
     * Validates URL format.
     */
    public static ValidatorRule<String> isUrl() {
        return (fieldName, value) -> {
            if (value != null) {
                try {
                    new java.net.URL(value).toURI();
                } catch (java.net.MalformedURLException | java.net.URISyntaxException e) {
                    return new ValidationError(fieldName, "must be a valid URL", "isUrl", value);
                }
            }
            return null;
        };
    }

    /**
     * Validates UUID format.
     */
    public static ValidatorRule<String> isUuid() {
        return (fieldName, value) -> {
            if (value != null && !UUID_PATTERN.matcher(value).matches()) {
                return new ValidationError(fieldName, "must be a valid UUID", "isUuid", value);
            }
            return null;
        };
    }

    /**
     * Validates IPv4 or IPv6 address.
     */
    public static ValidatorRule<String> isIpAddress() {
        return (fieldName, value) -> {
            if (value != null) {
                // Try IPv4 first
                if (IPV4_PATTERN.matcher(value).matches()) {
                    return null;
                }
                // Try parsing as IPv6 using InetAddress
                try {
                    java.net.InetAddress addr = java.net.InetAddress.getByName(value);
                    if (addr instanceof java.net.Inet6Address) {
                        return null;
                    }
                } catch (java.net.UnknownHostException e) {
                    // Fall through to error
                }
                return new ValidationError(fieldName, "must be a valid IP address", "isIpAddress", value);
            }
            return null;
        };
    }

    /**
     * Validates phone number format.
     */
    public static ValidatorRule<String> isPhoneNumber() {
        return (fieldName, value) -> {
            if (value != null && !PHONE_PATTERN.matcher(value).matches()) {
                return new ValidationError(fieldName, "must be a valid phone number", "isPhoneNumber", value);
            }
            return null;
        };
    }

    /**
     * Validates minimum string length.
     */
    public static ValidatorRule<String> minLength(int minLength) {
        return (fieldName, value) -> {
            if (value != null && value.length() < minLength) {
                return new ValidationError(fieldName, "must be at least " + minLength + " characters", "minLength", value);
            }
            return null;
        };
    }

    /**
     * Validates maximum string length.
     */
    public static ValidatorRule<String> maxLength(int maxLength) {
        return (fieldName, value) -> {
            if (value != null && value.length() > maxLength) {
                return new ValidationError(fieldName, "must be at most " + maxLength + " characters", "maxLength", value);
            }
            return null;
        };
    }

    /**
     * Validates string length range.
     */
    public static ValidatorRule<String> length(int minLength, int maxLength) {
        return (fieldName, value) -> {
            if (value != null && (value.length() < minLength || value.length() > maxLength)) {
                return new ValidationError(fieldName, "must be between " + minLength + " and " + maxLength + " characters", "length", value);
            }
            return null;
        };
    }

    /**
     * Validates minimum numeric value.
     */
    public static ValidatorRule<Number> min(Number minValue) {
        return (fieldName, value) -> {
            if (value != null && value.doubleValue() < minValue.doubleValue()) {
                return new ValidationError(fieldName, "must be at least " + minValue, "min", value);
            }
            return null;
        };
    }

    /**
     * Validates maximum numeric value.
     */
    public static ValidatorRule<Number> max(Number maxValue) {
        return (fieldName, value) -> {
            if (value != null && value.doubleValue() > maxValue.doubleValue()) {
                return new ValidationError(fieldName, "must be at most " + maxValue, "max", value);
            }
            return null;
        };
    }

    /**
     * Validates numeric range.
     */
    public static ValidatorRule<Number> range(Number minValue, Number maxValue) {
        return (fieldName, value) -> {
            if (value != null && (value.doubleValue() < minValue.doubleValue() || value.doubleValue() > maxValue.doubleValue())) {
                return new ValidationError(fieldName, "must be between " + minValue + " and " + maxValue, "range", value);
            }
            return null;
        };
    }

    /**
     * Validates against a regex pattern.
     */
    public static ValidatorRule<String> pattern(String regex) {
        Pattern pattern = Pattern.compile(regex);
        return (fieldName, value) -> {
            if (value != null && !pattern.matcher(value).matches()) {
                return new ValidationError(fieldName, "must match pattern: " + regex, "pattern", value);
            }
            return null;
        };
    }

    /**
     * Validates that string contains only alphabetic characters.
     */
    public static ValidatorRule<String> isAlpha() {
        return (fieldName, value) -> {
            if (value != null && !ALPHA_PATTERN.matcher(value).matches()) {
                return new ValidationError(fieldName, "must contain only alphabetic characters", "isAlpha", value);
            }
            return null;
        };
    }

    /**
     * Validates that string contains only alphanumeric characters.
     */
    public static ValidatorRule<String> isAlphanumeric() {
        return (fieldName, value) -> {
            if (value != null && !ALPHANUMERIC_PATTERN.matcher(value).matches()) {
                return new ValidationError(fieldName, "must contain only alphanumeric characters", "isAlphanumeric", value);
            }
            return null;
        };
    }

    /**
     * Validates that string contains only numeric characters.
     */
    public static ValidatorRule<String> isNumeric() {
        return (fieldName, value) -> {
            if (value != null && !NUMERIC_PATTERN.matcher(value).matches()) {
                return new ValidationError(fieldName, "must contain only numeric characters", "isNumeric", value);
            }
            return null;
        };
    }

    /**
     * Validates date format.
     */
    public static ValidatorRule<String> isDate(String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return (fieldName, value) -> {
            if (value != null) {
                try {
                    LocalDate.parse(value, formatter);
                } catch (DateTimeParseException e) {
                    return new ValidationError(fieldName, "must be a valid date in format: " + pattern, "isDate", value);
                }
            }
            return null;
        };
    }

    /**
     * Validates that date is before the given date.
     */
    public static ValidatorRule<LocalDate> isBefore(LocalDate referenceDate) {
        return (fieldName, value) -> {
            if (value != null && !value.isBefore(referenceDate)) {
                return new ValidationError(fieldName, "must be before " + referenceDate, "isBefore", value);
            }
            return null;
        };
    }

    /**
     * Validates that date is after the given date.
     */
    public static ValidatorRule<LocalDate> isAfter(LocalDate referenceDate) {
        return (fieldName, value) -> {
            if (value != null && !value.isAfter(referenceDate)) {
                return new ValidationError(fieldName, "must be after " + referenceDate, "isAfter", value);
            }
            return null;
        };
    }

    /**
     * Validates that value is in the given collection.
     */
    public static <T> ValidatorRule<T> isIn(Collection<T> allowedValues) {
        return (fieldName, value) -> {
            if (value != null && !allowedValues.contains(value)) {
                return new ValidationError(fieldName, "must be one of: " + allowedValues, "isIn", value);
            }
            return null;
        };
    }

    /**
     * Validates that value is not in the given collection.
     */
    public static <T> ValidatorRule<T> isNotIn(Collection<T> disallowedValues) {
        return (fieldName, value) -> {
            if (value != null && disallowedValues.contains(value)) {
                return new ValidationError(fieldName, "must not be one of: " + disallowedValues, "isNotIn", value);
            }
            return null;
        };
    }
}
