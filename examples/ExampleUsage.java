package com.validator.examples;

import com.validator.core.Validator;
import com.validator.result.ValidationResult;
import com.validator.sanitizers.Sanitizers;
import com.validator.validators.Validators;

/**
 * Example demonstrating the Java Validation and Sanitization Library.
 */
public class ExampleUsage {

    public static void main(String[] args) {
        System.out.println("=== Java Validation and Sanitization Library Examples ===\n");

        // Example 1: Simple Validation
        simpleValidation();

        // Example 2: Validation with Sanitization
        validationWithSanitization();

        // Example 3: Real-world User Registration
        userRegistration();

        // Example 4: Content Sanitization
        contentSanitization();
    }

    private static void simpleValidation() {
        System.out.println("1. Simple Validation Example:");
        System.out.println("-------------------------------");

        ValidationResult result = Validator.create()
            .field("email", "user@example.com")
                .validate(Validators.notBlank())
                .validate(Validators.isEmail())
            .field("age", (Number) 25)
                .validate(Validators.notNull())
                .validate(Validators.range(18, 120))
            .execute();

        if (result.isValid()) {
            System.out.println("✓ Validation passed!");
            System.out.println("  Email: " + result.getSanitizedValue("email"));
            System.out.println("  Age: " + result.getSanitizedValue("age"));
        } else {
            System.out.println("✗ Validation failed:");
            result.getErrors().forEach(error ->
                System.out.println("  - " + error.getFieldName() + ": " + error.getMessage())
            );
        }
        System.out.println();
    }

    private static void validationWithSanitization() {
        System.out.println("2. Validation with Sanitization Example:");
        System.out.println("-----------------------------------------");

        String rawUsername = "  JohnDoe123  ";
        System.out.println("Raw username: '" + rawUsername + "'");

        ValidationResult result = Validator.create()
            .field("username", rawUsername)
                .sanitize(Sanitizers.trim())
                .sanitize(Sanitizers.toLowerCase())
                .validate(Validators.notBlank())
                .validate(Validators.isAlphanumeric())
                .validate(Validators.minLength(3))
                .validate(Validators.maxLength(20))
            .execute();

        if (result.isValid()) {
            String cleanUsername = result.getSanitizedValue("username");
            System.out.println("✓ Validation passed!");
            System.out.println("  Clean username: '" + cleanUsername + "'");
        } else {
            System.out.println("✗ Validation failed:");
            result.getErrors().forEach(error ->
                System.out.println("  - " + error.getMessage())
            );
        }
        System.out.println();
    }

    private static void userRegistration() {
        System.out.println("3. Real-world User Registration Example:");
        System.out.println("-----------------------------------------");

        // Simulating user input
        String username = "  John_Doe_123  ";
        String email = " USER@EXAMPLE.COM ";
        String password = "SecurePass123!";
        int age = 25;

        ValidationResult result = Validator.create()
            .field("username", username)
                .sanitize(Sanitizers.trim())
                .sanitize(Sanitizers.toLowerCase())
                .validate(Validators.notBlank())
                .validate(Validators.minLength(3))
                .validate(Validators.maxLength(20))
            .field("email", email)
                .sanitize(Sanitizers.trim())
                .sanitize(Sanitizers.toLowerCase())
                .validate(Validators.notBlank())
                .validate(Validators.isEmail())
            .field("password", password)
                .validate(Validators.notBlank())
                .validate(Validators.minLength(8))
            .field("age", (Number) age)
                .validate(Validators.notNull())
                .validate(Validators.range(18, 120))
            .execute();

        if (result.isValid()) {
            System.out.println("✓ User registration data is valid!");
            System.out.println("  Username: " + result.getSanitizedValue("username"));
            System.out.println("  Email: " + result.getSanitizedValue("email"));
            System.out.println("  Password: " + "********");
            System.out.println("  Age: " + result.getSanitizedValue("age"));
            System.out.println("  → Ready to save to database");
        } else {
            System.out.println("✗ User registration failed:");
            result.getErrors().forEach(error ->
                System.out.println("  - " + error.getFieldName() + ": " + error.getMessage())
            );
        }
        System.out.println();
    }

    private static void contentSanitization() {
        System.out.println("4. Content Sanitization Example (XSS Prevention):");
        System.out.println("--------------------------------------------------");

        String maliciousContent = "<script>alert('XSS attack!')</script>Hello World<p>Safe content</p>";
        System.out.println("Raw content: " + maliciousContent);

        ValidationResult result = Validator.create()
            .field("content", maliciousContent)
                .sanitize(Sanitizers.removeHtml())
                .sanitize(Sanitizers.escapeXss())
                .sanitize(Sanitizers.normalizeWhitespace())
                .validate(Validators.notBlank())
                .validate(Validators.maxLength(1000))
            .execute();

        if (result.isValid()) {
            String cleanContent = result.getSanitizedValue("content");
            System.out.println("✓ Content sanitized successfully!");
            System.out.println("  Clean content: " + cleanContent);
            System.out.println("  → Safe to display to users");
        } else {
            System.out.println("✗ Content validation failed:");
            result.getErrors().forEach(error ->
                System.out.println("  - " + error.getMessage())
            );
        }
        System.out.println();
    }
}
