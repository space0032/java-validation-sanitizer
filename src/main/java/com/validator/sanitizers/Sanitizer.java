package com.validator.sanitizers;

/**
 * Entry point for creating sanitizers.
 */
public class Sanitizer {
    /**
     * Creates a new sanitizer builder for the given input value.
     * 
     * @param input the input value to sanitize
     * @return a new sanitizer builder
     */
    public static SanitizerBuilder create(String input) {
        return new SanitizerBuilder(input);
    }
}
