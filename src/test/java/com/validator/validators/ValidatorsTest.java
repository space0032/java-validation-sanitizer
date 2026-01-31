package com.validator.validators;

import com.validator.core.ValidatorRule;
import com.validator.result.ValidationError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorsTest {

    @Test
    void testNotNull() {
        ValidatorRule<String> rule = Validators.notNull();
        
        assertNull(rule.validate("field", "value"));
        assertNotNull(rule.validate("field", null));
    }

    @Test
    void testNotEmpty() {
        ValidatorRule<String> rule = Validators.notEmpty();
        
        assertNull(rule.validate("field", "value"));
        assertNotNull(rule.validate("field", ""));
        assertNotNull(rule.validate("field", null));
    }

    @Test
    void testNotBlank() {
        ValidatorRule<String> rule = Validators.notBlank();
        
        assertNull(rule.validate("field", "value"));
        assertNotNull(rule.validate("field", "   "));
        assertNotNull(rule.validate("field", ""));
        assertNotNull(rule.validate("field", null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"user@example.com", "test.user@domain.co.uk", "a+b@test.com"})
    void testIsEmailValid(String email) {
        ValidatorRule<String> rule = Validators.isEmail();
        assertNull(rule.validate("email", email));
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid", "user@", "@example.com", "user @example.com", "user@example..com", ".user@example.com", "user.@example.com"})
    void testIsEmailInvalid(String email) {
        ValidatorRule<String> rule = Validators.isEmail();
        assertNotNull(rule.validate("email", email));
    }

    @ParameterizedTest
    @ValueSource(strings = {"http://example.com", "https://test.org", "ftp://files.server.com"})
    void testIsUrlValid(String url) {
        ValidatorRule<String> rule = Validators.isUrl();
        assertNull(rule.validate("url", url));
    }

    @ParameterizedTest
    @ValueSource(strings = {"not-a-url", "htp://wrong.com", "example.com"})
    void testIsUrlInvalid(String url) {
        ValidatorRule<String> rule = Validators.isUrl();
        assertNotNull(rule.validate("url", url));
    }

    @Test
    void testIsUuidValid() {
        ValidatorRule<String> rule = Validators.isUuid();
        assertNull(rule.validate("id", "123e4567-e89b-12d3-a456-426614174000"));
    }

    @Test
    void testIsUuidInvalid() {
        ValidatorRule<String> rule = Validators.isUuid();
        assertNotNull(rule.validate("id", "not-a-uuid"));
        assertNotNull(rule.validate("id", "123e4567-e89b-12d3-a456"));
    }

    @Test
    void testIsIpAddressValid() {
        ValidatorRule<String> rule = Validators.isIpAddress();
        assertNull(rule.validate("ip", "192.168.1.1"));
        assertNull(rule.validate("ip", "0.0.0.0"));
        assertNull(rule.validate("ip", "255.255.255.255"));
        // IPv6 tests
        assertNull(rule.validate("ip", "2001:db8::1"));
        assertNull(rule.validate("ip", "::1"));
        assertNull(rule.validate("ip", "fe80::1"));
        assertNull(rule.validate("ip", "2001:0db8:0000:0000:0000:0000:0000:0001"));
    }

    @Test
    void testIsIpAddressInvalid() {
        ValidatorRule<String> rule = Validators.isIpAddress();
        assertNotNull(rule.validate("ip", "256.1.1.1"));
        assertNotNull(rule.validate("ip", "192.168"));
        assertNotNull(rule.validate("ip", "not-an-ip"));
    }

    @Test
    void testIsPhoneNumberValid() {
        ValidatorRule<String> rule = Validators.isPhoneNumber();
        assertNull(rule.validate("phone", "1234567890"));
        assertNull(rule.validate("phone", "+1-234-567-8900"));
        assertNull(rule.validate("phone", "(123) 456-7890"));
    }

    @Test
    void testMinLength() {
        ValidatorRule<String> rule = Validators.minLength(5);
        
        assertNull(rule.validate("field", "12345"));
        assertNull(rule.validate("field", "123456"));
        assertNotNull(rule.validate("field", "1234"));
    }

    @Test
    void testMaxLength() {
        ValidatorRule<String> rule = Validators.maxLength(5);
        
        assertNull(rule.validate("field", "12345"));
        assertNull(rule.validate("field", "1234"));
        assertNotNull(rule.validate("field", "123456"));
    }

    @Test
    void testLength() {
        ValidatorRule<String> rule = Validators.length(3, 5);
        
        assertNull(rule.validate("field", "123"));
        assertNull(rule.validate("field", "1234"));
        assertNull(rule.validate("field", "12345"));
        assertNotNull(rule.validate("field", "12"));
        assertNotNull(rule.validate("field", "123456"));
    }

    @Test
    void testMin() {
        ValidatorRule<Number> rule = Validators.min(10);
        
        assertNull(rule.validate("field", 10));
        assertNull(rule.validate("field", 15));
        assertNotNull(rule.validate("field", 5));
    }

    @Test
    void testMax() {
        ValidatorRule<Number> rule = Validators.max(10);
        
        assertNull(rule.validate("field", 10));
        assertNull(rule.validate("field", 5));
        assertNotNull(rule.validate("field", 15));
    }

    @Test
    void testRange() {
        ValidatorRule<Number> rule = Validators.range(10, 20);
        
        assertNull(rule.validate("field", 10));
        assertNull(rule.validate("field", 15));
        assertNull(rule.validate("field", 20));
        assertNotNull(rule.validate("field", 5));
        assertNotNull(rule.validate("field", 25));
    }

    @Test
    void testPattern() {
        ValidatorRule<String> rule = Validators.pattern("^[A-Z]{3}$");
        
        assertNull(rule.validate("field", "ABC"));
        assertNotNull(rule.validate("field", "AB"));
        assertNotNull(rule.validate("field", "abc"));
        assertNotNull(rule.validate("field", "ABCD"));
    }

    @Test
    void testIsAlpha() {
        ValidatorRule<String> rule = Validators.isAlpha();
        
        assertNull(rule.validate("field", "abc"));
        assertNull(rule.validate("field", "ABC"));
        assertNotNull(rule.validate("field", "abc123"));
        assertNotNull(rule.validate("field", "abc "));
    }

    @Test
    void testIsAlphanumeric() {
        ValidatorRule<String> rule = Validators.isAlphanumeric();
        
        assertNull(rule.validate("field", "abc123"));
        assertNull(rule.validate("field", "ABC123"));
        assertNotNull(rule.validate("field", "abc-123"));
        assertNotNull(rule.validate("field", "abc 123"));
    }

    @Test
    void testIsNumeric() {
        ValidatorRule<String> rule = Validators.isNumeric();
        
        assertNull(rule.validate("field", "123"));
        assertNotNull(rule.validate("field", "12.3"));
        assertNotNull(rule.validate("field", "abc"));
    }

    @Test
    void testIsDate() {
        ValidatorRule<String> rule = Validators.isDate("yyyy-MM-dd");
        
        assertNull(rule.validate("field", "2023-12-31"));
        assertNotNull(rule.validate("field", "12/31/2023"));
        assertNotNull(rule.validate("field", "not-a-date"));
    }

    @Test
    void testIsBefore() {
        LocalDate reference = LocalDate.of(2023, 12, 31);
        ValidatorRule<LocalDate> rule = Validators.isBefore(reference);
        
        assertNull(rule.validate("field", LocalDate.of(2023, 12, 30)));
        assertNotNull(rule.validate("field", LocalDate.of(2023, 12, 31)));
        assertNotNull(rule.validate("field", LocalDate.of(2024, 1, 1)));
    }

    @Test
    void testIsAfter() {
        LocalDate reference = LocalDate.of(2023, 12, 31);
        ValidatorRule<LocalDate> rule = Validators.isAfter(reference);
        
        assertNull(rule.validate("field", LocalDate.of(2024, 1, 1)));
        assertNotNull(rule.validate("field", LocalDate.of(2023, 12, 31)));
        assertNotNull(rule.validate("field", LocalDate.of(2023, 12, 30)));
    }

    @Test
    void testIsIn() {
        ValidatorRule<String> rule = Validators.isIn(Arrays.asList("red", "green", "blue"));
        
        assertNull(rule.validate("field", "red"));
        assertNull(rule.validate("field", "green"));
        assertNotNull(rule.validate("field", "yellow"));
    }

    @Test
    void testIsNotIn() {
        ValidatorRule<String> rule = Validators.isNotIn(Arrays.asList("admin", "root", "system"));
        
        assertNull(rule.validate("field", "user"));
        assertNotNull(rule.validate("field", "admin"));
        assertNotNull(rule.validate("field", "root"));
    }

    @Test
    void testNullValuesAreHandledGracefully() {
        // Most validators should return null (pass) for null values except notNull
        assertNull(Validators.isEmail().validate("field", null));
        assertNull(Validators.minLength(5).validate("field", null));
        assertNull(Validators.min(10).validate("field", null));
    }
}
