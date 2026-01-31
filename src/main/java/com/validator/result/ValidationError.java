package com.validator.result;

/**
 * Represents a validation error for a specific field.
 */
public class ValidationError {
    private final String fieldName;
    private final String message;
    private final String code;
    private final Object rejectedValue;

    /**
     * Creates a new ValidationError.
     * 
     * @param fieldName the name of the field that failed validation
     * @param message the error message
     * @param code the error code
     */
    public ValidationError(String fieldName, String message, String code) {
        this(fieldName, message, code, null);
    }

    /**
     * Creates a new ValidationError with a rejected value.
     * 
     * @param fieldName the name of the field that failed validation
     * @param message the error message
     * @param code the error code
     * @param rejectedValue the value that was rejected
     */
    public ValidationError(String fieldName, String message, String code, Object rejectedValue) {
        this.fieldName = fieldName;
        this.message = message;
        this.code = code;
        this.rejectedValue = rejectedValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }

    @Override
    public String toString() {
        return "ValidationError{" +
                "fieldName='" + fieldName + '\'' +
                ", message='" + message + '\'' +
                ", code='" + code + '\'' +
                ", rejectedValue=" + rejectedValue +
                '}';
    }
}
