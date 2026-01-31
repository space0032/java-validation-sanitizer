package com.validator.result;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the result of a validation operation.
 */
public class ValidationResult {
    private final List<ValidationError> errors;
    private final Map<String, Object> sanitizedValues;

    public ValidationResult(List<ValidationError> errors, Map<String, Object> sanitizedValues) {
        this.errors = Collections.unmodifiableList(new ArrayList<>(errors));
        this.sanitizedValues = Collections.unmodifiableMap(new HashMap<>(sanitizedValues));
    }

    /**
     * Checks if the validation passed (no errors).
     * 
     * @return true if validation passed, false otherwise
     */
    public boolean isValid() {
        return errors.isEmpty();
    }

    /**
     * Gets all validation errors.
     * 
     * @return unmodifiable list of validation errors
     */
    public List<ValidationError> getErrors() {
        return errors;
    }

    /**
     * Gets validation errors for a specific field.
     * 
     * @param fieldName the field name
     * @return list of errors for the field
     */
    public List<ValidationError> getErrorsForField(String fieldName) {
        return errors.stream()
                .filter(e -> e.getFieldName().equals(fieldName))
                .collect(Collectors.toList());
    }

    /**
     * Gets all sanitized values.
     * 
     * @return unmodifiable map of sanitized values
     */
    public Map<String, Object> getSanitizedValues() {
        return sanitizedValues;
    }

    /**
     * Gets a sanitized value for a specific field.
     * 
     * @param fieldName the field name
     * @param <T> the expected type
     * @return the sanitized value, or null if not found
     */
    @SuppressWarnings("unchecked")
    public <T> T getSanitizedValue(String fieldName) {
        return (T) sanitizedValues.get(fieldName);
    }

    /**
     * Gets a sanitized value for a specific field with type checking.
     * 
     * @param fieldName the field name
     * @param type the expected class type
     * @param <T> the expected type
     * @return Optional containing the sanitized value if found and type matches, empty otherwise
     */
    public <T> java.util.Optional<T> getSanitizedValue(String fieldName, Class<T> type) {
        Object value = sanitizedValues.get(fieldName);
        if (value != null && type.isInstance(value)) {
            return java.util.Optional.of(type.cast(value));
        }
        return java.util.Optional.empty();
    }

    /**
     * Gets a combined error message with all validation errors.
     * 
     * @return combined error message
     */
    public String getErrorMessage() {
        if (errors.isEmpty()) {
            return "";
        }
        return errors.stream()
                .map(e -> e.getFieldName() + ": " + e.getMessage())
                .collect(Collectors.joining("; "));
    }

    @Override
    public String toString() {
        return "ValidationResult{" +
                "valid=" + isValid() +
                ", errors=" + errors +
                ", sanitizedValues=" + sanitizedValues +
                '}';
    }
}
