package com.validator.sanitizers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SanitizersTest {

    @Test
    void testTrim() {
        assertEquals("test", Sanitizers.trim().sanitize("  test  "));
        assertEquals("", Sanitizers.trim().sanitize("   "));
        assertNull(Sanitizers.trim().sanitize(null));
    }

    @Test
    void testToLowerCase() {
        assertEquals("test", Sanitizers.toLowerCase().sanitize("TEST"));
        assertEquals("test", Sanitizers.toLowerCase().sanitize("TeSt"));
        assertNull(Sanitizers.toLowerCase().sanitize(null));
    }

    @Test
    void testToUpperCase() {
        assertEquals("TEST", Sanitizers.toUpperCase().sanitize("test"));
        assertEquals("TEST", Sanitizers.toUpperCase().sanitize("TeSt"));
        assertNull(Sanitizers.toUpperCase().sanitize(null));
    }

    @Test
    void testRemoveHtml() {
        assertEquals("Hello World", Sanitizers.removeHtml().sanitize("<p>Hello World</p>"));
        // jsoup removes the content inside script tags along with the tag
        assertEquals("", Sanitizers.removeHtml().sanitize("<script>alert('xss')</script>"));
        assertEquals("", Sanitizers.removeHtml().sanitize("<div></div>"));
        assertNull(Sanitizers.removeHtml().sanitize(null));
    }

    @Test
    void testEscapeHtml() {
        String result = Sanitizers.escapeHtml().sanitize("<script>alert('xss')</script>");
        assertTrue(result.contains("&lt;") && result.contains("&gt;"));
        assertNull(Sanitizers.escapeHtml().sanitize(null));
    }

    @Test
    void testEscapeXss() {
        String result = Sanitizers.escapeXss().sanitize("<script>alert('xss')</script>");
        assertFalse(result.contains("<script>"));
        assertNull(Sanitizers.escapeXss().sanitize(null));
    }

    @Test
    void testRemoveNonPrintable() {
        assertEquals("test", Sanitizers.removeNonPrintable().sanitize("test\u0000\u0001"));
        assertEquals("hello world", Sanitizers.removeNonPrintable().sanitize("hello\u0007 world"));
        assertNull(Sanitizers.removeNonPrintable().sanitize(null));
    }

    @Test
    void testRemoveNonAlphanumeric() {
        assertEquals("test123", Sanitizers.removeNonAlphanumeric().sanitize("test-123!"));
        assertEquals("HelloWorld", Sanitizers.removeNonAlphanumeric().sanitize("Hello, World!"));
        assertNull(Sanitizers.removeNonAlphanumeric().sanitize(null));
    }

    @Test
    void testRemoveNonNumeric() {
        assertEquals("123", Sanitizers.removeNonNumeric().sanitize("test123"));
        assertEquals("42", Sanitizers.removeNonNumeric().sanitize("4.2"));
        assertNull(Sanitizers.removeNonNumeric().sanitize(null));
    }

    @Test
    void testRemoveNonAlpha() {
        assertEquals("test", Sanitizers.removeNonAlpha().sanitize("test123"));
        assertEquals("HelloWorld", Sanitizers.removeNonAlpha().sanitize("Hello World!"));
        assertNull(Sanitizers.removeNonAlpha().sanitize(null));
    }

    @Test
    void testNormalizeWhitespace() {
        assertEquals("hello world", Sanitizers.normalizeWhitespace().sanitize("hello   world"));
        assertEquals("a b c", Sanitizers.normalizeWhitespace().sanitize("a  b    c"));
        assertEquals("test", Sanitizers.normalizeWhitespace().sanitize("  test  "));
        assertNull(Sanitizers.normalizeWhitespace().sanitize(null));
    }

    @Test
    void testMaxLength() {
        assertEquals("12345", Sanitizers.maxLength(5).sanitize("12345"));
        assertEquals("12345", Sanitizers.maxLength(5).sanitize("123456789"));
        assertEquals("test", Sanitizers.maxLength(10).sanitize("test"));
        assertNull(Sanitizers.maxLength(5).sanitize(null));
    }

    @Test
    void testRemovePattern() {
        assertEquals("hello world", Sanitizers.removePattern("\\d+").sanitize("hello123 world456"));
        assertEquals("test", Sanitizers.removePattern("[^a-z]").sanitize("test123"));
        assertNull(Sanitizers.removePattern("\\d+").sanitize(null));
    }

    @Test
    void testReplacePattern() {
        assertEquals("hello_world", Sanitizers.replacePattern("\\s+", "_").sanitize("hello world"));
        assertEquals("xxx123", Sanitizers.replacePattern("[a-z]+", "xxx").sanitize("test123"));
        assertNull(Sanitizers.replacePattern("\\s+", "_").sanitize(null));
    }

    @Test
    void testStripSqlKeywords() {
        String input = "SELECT * FROM users WHERE name = 'test'";
        String result = Sanitizers.stripSqlKeywords().sanitize(input);
        // The stripSqlKeywords removes the keywords but may leave other parts
        assertTrue(result.length() < input.length() || !result.toUpperCase().contains("SELECT"));
        
        // Test additional keywords
        assertEquals("", Sanitizers.stripSqlKeywords().sanitize("TRUNCATE"));
        assertEquals("", Sanitizers.stripSqlKeywords().sanitize("MERGE"));
        assertEquals(" data", Sanitizers.stripSqlKeywords().sanitize("GRANT data"));
        
        assertNull(Sanitizers.stripSqlKeywords().sanitize(null));
    }

    @Test
    void testStripSqlComments() {
        assertEquals("SELECT * FROM users ", Sanitizers.stripSqlComments().sanitize("SELECT * FROM users -- comment"));
        assertEquals("SELECT * FROM users ", Sanitizers.stripSqlComments().sanitize("SELECT * FROM users /* comment */"));
        assertEquals("SELECT  FROM users", Sanitizers.stripSqlComments().sanitize("SELECT /* inline */ FROM users"));
        assertNull(Sanitizers.stripSqlComments().sanitize(null));
    }

    @Test
    void testAllowOnly() {
        assertEquals("123", Sanitizers.allowOnly("0123456789").sanitize("abc123def"));
        assertEquals("abc", Sanitizers.allowOnly("abc").sanitize("abcxyz"));
        assertNull(Sanitizers.allowOnly("0123456789").sanitize(null));
    }

    @Test
    void testDeny() {
        assertEquals("abc", Sanitizers.deny("0123456789").sanitize("abc123"));
        assertEquals("test", Sanitizers.deny("!@#$").sanitize("test!@#"));
        assertNull(Sanitizers.deny("0123456789").sanitize(null));
    }

    @Test
    void testSanitizerChaining() {
        String result = Sanitizer.create("  HELLO WORLD  ")
            .trim()
            .toLowerCase()
            .sanitize();
        assertEquals("hello world", result);
    }

    @Test
    void testComplexSanitizerChaining() {
        String result = Sanitizer.create("<p>Hello, World!</p>")
            .removeHtml()
            .removeNonAlphanumeric()
            .toLowerCase()
            .sanitize();
        assertEquals("helloworld", result);
    }

    @Test
    void testSanitizerWithNull() {
        String result = Sanitizer.create(null)
            .trim()
            .toLowerCase()
            .sanitize();
        assertNull(result);
    }
}
