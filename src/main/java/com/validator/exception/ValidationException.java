package com.validator.exception;

import com.validator.result.ValidationResult;

/**
 * Exception thrown when validation fails in fail-fast mode.
 */
public class ValidationException extends RuntimeException {
    private final ValidationResult validationResult;

    public ValidationException(String message, ValidationResult validationResult) {
        super(message);
        this.validationResult = validationResult;
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }
}
