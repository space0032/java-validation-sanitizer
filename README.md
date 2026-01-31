# Java Validation and Sanitization Library

A comprehensive Java library for advanced validation and sanitization that goes beyond what `javax.validation` and Spring Validator provide. This library offers a fluent API for strict sanitization and business-rule validations.

## Features

- **Fluent API**: Chain validations and sanitizations in a natural, readable way
- **Rich Validator Set**: 20+ built-in validators for strings, numbers, dates, and more
- **Comprehensive Sanitization**: 15+ sanitizers for cleaning and normalizing data
- **XSS Protection**: Built-in OWASP encoder integration for preventing XSS attacks
- **HTML Sanitization**: jsoup-powered HTML cleaning
- **Custom Validators**: Easy-to-create custom validation rules
- **Thread-Safe**: Immutable result objects and thread-safe implementation
- **Zero Dependencies**: No required runtime dependencies on Spring or Jakarta EE
- **Java 11+**: Modern Java with lambda support

## Installation

### Maven

Add this dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.validator</groupId>
    <artifactId>java-validation-sanitizer</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```gradle
implementation 'com.validator:java-validation-sanitizer:1.0.0'
```

## Quick Start

### Simple Validation

```java
import com.validator.core.Validator;
import com.validator.validators.Validators;
import com.validator.result.ValidationResult;

ValidationResult result = Validator.create()
    .field("email", "user@example.com")
        .validate(Validators.notBlank())
        .validate(Validators.isEmail())
    .field("age", 25)
        .validate(Validators.notNull())
        .validate(Validators.range(18, 120))
    .execute();

if (!result.isValid()) {
    result.getErrors().forEach(error -> 
        System.out.println(error.getFieldName() + ": " + error.getMessage())
    );
}
```

### Validation with Sanitization

```java
import com.validator.sanitizers.Sanitizers;

ValidationResult result = Validator.create()
    .field("username", "  JohnDoe123  ")
        .sanitize(Sanitizers.trim())
        .sanitize(Sanitizers.toLowerCase())
        .validate(Validators.notBlank())
        .validate(Validators.isAlphanumeric())
    .execute();

String cleanUsername = result.getSanitizedValue("username"); // "johndoe123"
```

### Standalone Sanitization

```java
import com.validator.sanitizers.Sanitizer;

String sanitized = Sanitizer.create("<script>alert('xss')</script>Hello World")
    .removeHtml()
    .escapeXss()
    .maxLength(100)
    .sanitize(); // "Hello World"
```

## Built-in Validators

### String Validators

- `notNull()` - Check if value is not null
- `notEmpty()` - Check if string is not empty
- `notBlank()` - Check if string is not blank (not null, not empty, not just whitespace)
- `isEmail()` - Validate email format
- `isUrl()` - Validate URL format
- `isUuid()` - Validate UUID format
- `isIpAddress()` - Validate IPv4/IPv6 addresses
- `isPhoneNumber()` - Validate phone number format
- `minLength(int)` - Minimum string length
- `maxLength(int)` - Maximum string length
- `length(int min, int max)` - String length range
- `pattern(String regex)` - Regex pattern matching
- `isAlpha()` - Only alphabetic characters
- `isAlphanumeric()` - Only alphanumeric characters
- `isNumeric()` - Only numeric characters

### Number Validators

- `min(Number)` - Minimum numeric value
- `max(Number)` - Maximum numeric value
- `range(Number min, Number max)` - Numeric range

### Date Validators

- `isDate(String pattern)` - Date format validation
- `isBefore(LocalDate)` - Date is before given date
- `isAfter(LocalDate)` - Date is after given date

### Collection Validators

- `isIn(Collection)` - Value is in collection
- `isNotIn(Collection)` - Value is not in collection

## Built-in Sanitizers

### Text Sanitizers

- `trim()` - Remove leading/trailing whitespace
- `toLowerCase()` - Convert to lowercase
- `toUpperCase()` - Convert to uppercase
- `normalizeWhitespace()` - Replace multiple spaces with single space
- `maxLength(int)` - Truncate to max length

### HTML/XSS Sanitizers

- `removeHtml()` - Strip all HTML tags
- `escapeHtml()` - Escape HTML entities
- `escapeXss()` - Escape XSS attack vectors

### Character Sanitizers

- `removeNonPrintable()` - Remove non-printable characters
- `removeNonAlphanumeric()` - Keep only alphanumeric
- `removeNonNumeric()` - Keep only numbers
- `removeNonAlpha()` - Keep only letters
- `allowOnly(String allowedChars)` - Whitelist characters
- `deny(String deniedChars)` - Blacklist characters

### Pattern Sanitizers

- `removePattern(String regex)` - Remove matching patterns
- `replacePattern(String regex, String replacement)` - Replace patterns

### Security Sanitizers

- `stripSqlKeywords()` - Remove common SQL injection keywords

## Advanced Usage

### Custom Validators

Create custom validation rules using the functional interface:

```java
ValidatorRule<String> customRule = (fieldName, value) -> {
    if (value != null && value.startsWith("admin")) {
        return new ValidationError(fieldName, "Cannot start with 'admin'", "custom.admin");
    }
    return null;
};

ValidationResult result = Validator.create()
    .field("username", "adminUser")
        .validate(customRule)
    .execute();
```

### Configuration Options

```java
ValidatorConfig config = ValidatorConfig.builder()
    .failFast(true) // Stop on first error
    .messagePrefix("validation.error.")
    .defaultLocale(Locale.US)
    .build();

Validator.create(config)
    .field("email", "invalid")
        .validate(Validators.isEmail())
    .execute(); // Throws ValidationException if failFast is true
```

### Fail-Fast Mode

```java
try {
    ValidatorConfig config = ValidatorConfig.builder()
        .failFast(true)
        .build();
        
    Validator.create(config)
        .field("email", "invalid")
            .validate(Validators.isEmail())
        .execute();
} catch (ValidationException e) {
    ValidationResult result = e.getValidationResult();
    // Handle validation errors
}
```

### Getting Validation Results

```java
ValidationResult result = Validator.create()
    .field("email", "invalid")
        .validate(Validators.isEmail())
    .field("age", 15)
        .validate(Validators.min(18))
    .execute();

// Check if valid
boolean isValid = result.isValid();

// Get all errors
List<ValidationError> errors = result.getErrors();

// Get errors for specific field
List<ValidationError> emailErrors = result.getErrorsForField("email");

// Get combined error message
String message = result.getErrorMessage();

// Get sanitized values
Map<String, Object> allValues = result.getSanitizedValues();
String email = result.getSanitizedValue("email");
```

## Real-World Examples

### User Registration Form

```java
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
    .field("age", 25)
        .validate(Validators.notNull())
        .validate(Validators.range(18, 120))
    .execute();

if (result.isValid()) {
    String username = result.getSanitizedValue("username"); // "john_doe_123"
    String email = result.getSanitizedValue("email"); // "user@example.com"
    // Save to database...
}
```

### Content Sanitization (Blog Post)

```java
ValidationResult result = Validator.create()
    .field("title", userInput.getTitle())
        .sanitize(Sanitizers.trim())
        .sanitize(Sanitizers.normalizeWhitespace())
        .validate(Validators.notBlank())
        .validate(Validators.maxLength(200))
    .field("content", userInput.getContent())
        .sanitize(Sanitizers.removeHtml())
        .sanitize(Sanitizers.escapeXss())
        .validate(Validators.notBlank())
        .validate(Validators.maxLength(10000))
    .execute();
```

### API Input Validation

```java
ValidationResult result = Validator.create()
    .field("apiKey", request.getApiKey())
        .sanitize(Sanitizers.trim())
        .validate(Validators.notBlank())
        .validate(Validators.isUuid())
    .field("endpoint", request.getEndpoint())
        .validate(Validators.isUrl())
    .field("ipAddress", request.getIpAddress())
        .validate(Validators.isIpAddress())
    .execute();

if (!result.isValid()) {
    throw new ApiValidationException(result.getErrorMessage());
}
```

## API Documentation

### ValidationResult

```java
boolean isValid()                              // Check if validation passed
List<ValidationError> getErrors()              // Get all errors
List<ValidationError> getErrorsForField(String) // Get errors for specific field
Map<String, Object> getSanitizedValues()       // Get all sanitized values
<T> T getSanitizedValue(String)                // Get specific sanitized value
String getErrorMessage()                       // Get combined error message
```

### ValidationError

```java
String getFieldName()      // Field that failed validation
String getMessage()        // Error message
String getCode()           // Error code
Object getRejectedValue()  // Value that was rejected
```

## Thread Safety

All validators and sanitizers are thread-safe. The `ValidationResult` objects are immutable, making them safe to share across threads.

## Performance

The library is designed for high performance:
- Validators use pre-compiled regex patterns
- Sanitizers are optimized for common use cases
- No reflection or runtime code generation
- Minimal object allocation

## Requirements

- Java 11 or higher
- Maven or Gradle for dependency management

## Dependencies

- **OWASP Java Encoder** - For XSS protection
- **jsoup** - For HTML sanitization
- **JUnit 5** - For testing (test scope only)

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Support

For issues, questions, or contributions, please visit the [GitHub repository](https://github.com/space0032/java-validation-sanitizer).

## Version History

### 1.0.0 (Initial Release)
- Complete validation and sanitization framework
- 20+ built-in validators
- 15+ built-in sanitizers
- Custom validator support
- Fluent API
- Thread-safe implementation
- Comprehensive test coverage