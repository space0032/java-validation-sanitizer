package com.validator.core;

/**
 * Entry point for creating validators.
 */
public class Validator {
    /**
     * Creates a new validator with default configuration.
     * 
     * @return a new validator builder
     */
    public static ValidatorBuilder create() {
        return new ValidatorBuilder(ValidatorConfig.builder().build());
    }

    /**
     * Creates a new validator with custom configuration.
     * 
     * @param config the validator configuration
     * @return a new validator builder
     */
    public static ValidatorBuilder create(ValidatorConfig config) {
        return new ValidatorBuilder(config);
    }
}
