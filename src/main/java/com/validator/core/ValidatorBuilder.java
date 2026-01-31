package com.validator.core;

import com.validator.exception.ValidationException;
import com.validator.result.ValidationError;
import com.validator.result.ValidationResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builder for creating and executing validations.
 */
public class ValidatorBuilder {
    private final ValidatorConfig config;
    private final List<FieldValidator<?>> fieldValidators;
    private final List<ValidationError> crossFieldErrors;

    public ValidatorBuilder(ValidatorConfig config) {
        this.config = config;
        this.fieldValidators = new ArrayList<>();
        this.crossFieldErrors = new ArrayList<>();
    }

    /**
     * Adds a field to validate.
     * 
     * @param fieldName the field name
     * @param value the field value
     * @param <T> the value type
     * @return a field validator for chaining
     */
    public <T> FieldValidator<T> field(String fieldName, T value) {
        FieldValidator<T> fieldValidator = new FieldValidator<>(fieldName, value, config, this);
        fieldValidators.add(fieldValidator);
        return fieldValidator;
    }

    /**
     * Adds a cross-field validation error.
     * 
     * @param error the validation error
     * @return this builder for chaining
     */
    public ValidatorBuilder addCrossFieldError(ValidationError error) {
        if (error != null) {
            crossFieldErrors.add(error);
        }
        return this;
    }

    /**
     * Executes all validations and returns the result.
     * 
     * @return the validation result
     */
    public ValidationResult execute() {
        List<ValidationError> allErrors = new ArrayList<>();
        Map<String, Object> sanitizedValues = new HashMap<>();

        // Collect errors and sanitized values from all fields
        for (FieldValidator<?> fieldValidator : fieldValidators) {
            allErrors.addAll(fieldValidator.getErrors());
            sanitizedValues.put(fieldValidator.getFieldName(), fieldValidator.getValue());
        }

        // Add cross-field errors
        allErrors.addAll(crossFieldErrors);

        ValidationResult result = new ValidationResult(allErrors, sanitizedValues);

        // If fail-fast is enabled and there are errors, throw exception
        if (config.isFailFast() && !result.isValid()) {
            throw new ValidationException("Validation failed", result);
        }

        return result;
    }
}
