package com.validator.core;

import com.validator.result.ValidationError;
import com.validator.sanitizers.SanitizerRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Validator for a single field with support for sanitization and validation.
 */
public class FieldValidator<T> {
    private final String fieldName;
    private T value;
    private final List<ValidationError> errors;
    private final ValidatorConfig config;
    private final ValidatorBuilder parentBuilder;

    public FieldValidator(String fieldName, T value, ValidatorConfig config, ValidatorBuilder parentBuilder) {
        this.fieldName = fieldName;
        this.value = value;
        this.errors = new ArrayList<>();
        this.config = config;
        this.parentBuilder = parentBuilder;
    }

    /**
     * Adds a validation rule.
     * 
     * @param rule the validation rule
     * @return this field validator for chaining
     */
    public FieldValidator<T> validate(ValidatorRule<T> rule) {
        if (!config.isFailFast() || errors.isEmpty()) {
            ValidationError error = rule.validate(fieldName, value);
            if (error != null) {
                errors.add(error);
            }
        }
        return this;
    }

    /**
     * Adds a sanitization rule (only works for String values).
     * 
     * @param rule the sanitization rule
     * @return this field validator for chaining
     */
    @SuppressWarnings("unchecked")
    public FieldValidator<T> sanitize(SanitizerRule rule) {
        if (value instanceof String) {
            value = (T) rule.sanitize((String) value);
        }
        return this;
    }

    /**
     * Adds another field to validate.
     * 
     * @param fieldName the field name
     * @param value the field value
     * @param <U> the value type
     * @return a new field validator
     */
    public <U> FieldValidator<U> field(String fieldName, U value) {
        return parentBuilder.field(fieldName, value);
    }

    /**
     * Executes validation and returns the result.
     * 
     * @return the validation result
     */
    public com.validator.result.ValidationResult execute() {
        return parentBuilder.execute();
    }

    String getFieldName() {
        return fieldName;
    }

    T getValue() {
        return value;
    }

    List<ValidationError> getErrors() {
        return errors;
    }
}
