package com.validator.core;

import com.validator.result.ValidationError;

/**
 * Functional interface for defining custom validation rules.
 * 
 * @param <T> the type of value to validate
 */
@FunctionalInterface
public interface ValidatorRule<T> {
    /**
     * Validates a field value and returns a ValidationError if validation fails.
     * 
     * @param fieldName the name of the field being validated
     * @param value the value to validate
     * @return ValidationError if validation fails, null if validation passes
     */
    ValidationError validate(String fieldName, T value);
}
