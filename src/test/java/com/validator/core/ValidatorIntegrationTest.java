package com.validator.core;

import com.validator.exception.ValidationException;
import com.validator.result.ValidationResult;
import com.validator.sanitizers.Sanitizers;
import com.validator.validators.Validators;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorIntegrationTest {

    @Test
    void testSimpleValidation() {
        ValidationResult result = Validator.create()
            .field("email", "user@example.com")
                .validate(Validators.notBlank())
                .validate(Validators.isEmail())
            .field("age", (Number) 25)
                .validate(Validators.notNull())
                .validate(Validators.range(18, 120))
            .execute();

        assertTrue(result.isValid());
        assertEquals(0, result.getErrors().size());
    }

    @Test
    void testValidationWithErrors() {
        ValidationResult result = Validator.create()
            .field("email", "invalid-email")
                .validate(Validators.isEmail())
            .field("age", (Number) 15)
                .validate(Validators.min(18))
            .execute();

        assertFalse(result.isValid());
        assertEquals(2, result.getErrors().size());
        assertEquals(1, result.getErrorsForField("email").size());
        assertEquals(1, result.getErrorsForField("age").size());
    }

    @Test
    void testValidationWithSanitization() {
        ValidationResult result = Validator.create()
            .field("username", "  JohnDoe123  ")
                .sanitize(Sanitizers.trim())
                .sanitize(Sanitizers.toLowerCase())
                .validate(Validators.notBlank())
                .validate(Validators.isAlphanumeric())
            .execute();

        assertTrue(result.isValid());
        assertEquals("johndoe123", result.getSanitizedValue("username"));
    }

    @Test
    void testComplexSanitizationAndValidation() {
        ValidationResult result = Validator.create()
            .field("bio", "<script>alert('xss')</script>Hello World")
                .sanitize(Sanitizers.removeHtml())
                .sanitize(Sanitizers.escapeXss())
                .validate(Validators.maxLength(500))
            .execute();

        assertTrue(result.isValid());
        String sanitizedBio = result.getSanitizedValue("bio");
        assertFalse(sanitizedBio.contains("<script>"));
    }

    @Test
    void testMultipleFields() {
        ValidationResult result = Validator.create()
            .field("firstName", "John")
                .validate(Validators.notBlank())
                .validate(Validators.isAlpha())
            .field("lastName", "Doe")
                .validate(Validators.notBlank())
                .validate(Validators.isAlpha())
            .field("email", "john.doe@example.com")
                .validate(Validators.notBlank())
                .validate(Validators.isEmail())
            .field("age", (Number) 30)
                .validate(Validators.range(18, 120))
            .execute();

        assertTrue(result.isValid());
        assertEquals("John", result.getSanitizedValue("firstName"));
        assertEquals("Doe", result.getSanitizedValue("lastName"));
        assertEquals("john.doe@example.com", result.getSanitizedValue("email"));
        assertEquals(Integer.valueOf(30), result.getSanitizedValue("age"));
    }

    @Test
    void testCustomValidator() {
        ValidatorRule<String> customRule = (fieldName, value) -> {
            if (value != null && value.startsWith("admin")) {
                return new com.validator.result.ValidationError(fieldName, "Cannot start with 'admin'", "custom.admin");
            }
            return null;
        };

        ValidationResult result = Validator.create()
            .field("username", "adminUser")
                .validate(customRule)
            .execute();

        assertFalse(result.isValid());
        assertEquals(1, result.getErrors().size());
        assertEquals("custom.admin", result.getErrors().get(0).getCode());
    }

    @Test
    void testFailFastMode() {
        ValidatorConfig config = ValidatorConfig.builder()
            .failFast(true)
            .build();

        assertThrows(ValidationException.class, () ->
            Validator.create(config)
                .field("email", "invalid")
                    .validate(Validators.isEmail())
                .execute()
        );
    }

    @Test
    void testValidatorConfig() {
        ValidatorConfig config = ValidatorConfig.builder()
            .messagePrefix("validation.error.")
            .defaultLocale(Locale.US)
            .failFast(false)
            .build();

        assertEquals("validation.error.", config.getMessagePrefix());
        assertEquals(Locale.US, config.getDefaultLocale());
        assertFalse(config.isFailFast());
    }

    @Test
    void testGetErrorMessage() {
        ValidationResult result = Validator.create()
            .field("email", "invalid")
                .validate(Validators.isEmail())
            .field("age", (Number) 15)
                .validate(Validators.min(18))
            .execute();

        String errorMessage = result.getErrorMessage();
        assertFalse(errorMessage.isEmpty());
        assertTrue(errorMessage.contains("email"));
        assertTrue(errorMessage.contains("age"));
    }

    @Test
    void testEmptyErrorMessageForValidResult() {
        ValidationResult result = Validator.create()
            .field("email", "user@example.com")
                .validate(Validators.isEmail())
            .execute();

        assertEquals("", result.getErrorMessage());
    }

    @Test
    void testNullAndEmptyValidation() {
        ValidationResult result = Validator.create()
            .field("required", (String) null)
                .validate(Validators.notNull())
            .field("notEmpty", "")
                .validate(Validators.notEmpty())
            .field("notBlank", "   ")
                .validate(Validators.notBlank())
            .execute();

        assertFalse(result.isValid());
        assertEquals(3, result.getErrors().size());
    }

    @Test
    void testSanitizedValuesMapContainsAllFields() {
        ValidationResult result = Validator.create()
            .field("field1", "value1")
                .validate(Validators.notBlank())
            .field("field2", "value2")
                .validate(Validators.notBlank())
            .execute();

        assertEquals(2, result.getSanitizedValues().size());
        assertTrue(result.getSanitizedValues().containsKey("field1"));
        assertTrue(result.getSanitizedValues().containsKey("field2"));
    }

    @Test
    void testDateValidation() {
        ValidationResult result = Validator.create()
            .field("startDate", LocalDate.of(2023, 1, 1))
                .validate(Validators.isBefore(LocalDate.of(2024, 1, 1)))
            .field("endDate", LocalDate.of(2023, 12, 31))
                .validate(Validators.isAfter(LocalDate.of(2023, 1, 1)))
            .execute();

        assertTrue(result.isValid());
    }

    @Test
    void testRealWorldScenario() {
        // Simulating a user registration form
        ValidationResult result = Validator.create()
            .field("username", "  John_Doe_123  ")
                .sanitize(Sanitizers.trim())
                .sanitize(Sanitizers.toLowerCase())
                .validate(Validators.notBlank())
                .validate(Validators.minLength(3))
                .validate(Validators.maxLength(20))
            .field("email", " USER@EXAMPLE.COM ")
                .sanitize(Sanitizers.trim())
                .sanitize(Sanitizers.toLowerCase())
                .validate(Validators.notBlank())
                .validate(Validators.isEmail())
            .field("password", "SecurePass123")
                .validate(Validators.notBlank())
                .validate(Validators.minLength(8))
            .field("age", (Number) 25)
                .validate(Validators.notNull())
                .validate(Validators.range(18, 120))
            .field("website", "https://example.com")
                .validate(Validators.isUrl())
            .execute();

        assertTrue(result.isValid());
        assertEquals("john_doe_123", result.getSanitizedValue("username"));
        assertEquals("user@example.com", result.getSanitizedValue("email"));
    }

    @Test
    void testTypeSafeGetSanitizedValue() {
        ValidationResult result = Validator.create()
            .field("username", "  JohnDoe  ")
                .sanitize(Sanitizers.trim())
                .validate(Validators.notBlank())
            .field("age", (Number) 25)
                .validate(Validators.notNull())
            .execute();

        assertTrue(result.isValid());
        
        // Test type-safe method with correct type
        java.util.Optional<String> username = result.getSanitizedValue("username", String.class);
        assertTrue(username.isPresent());
        assertEquals("JohnDoe", username.get());
        
        // Test type-safe method with incorrect type
        java.util.Optional<Integer> wrongType = result.getSanitizedValue("username", Integer.class);
        assertFalse(wrongType.isPresent());
        
        // Test type-safe method with non-existent field
        java.util.Optional<String> missing = result.getSanitizedValue("nonexistent", String.class);
        assertFalse(missing.isPresent());
    }
}
