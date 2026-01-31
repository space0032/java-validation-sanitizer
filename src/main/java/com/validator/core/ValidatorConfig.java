package com.validator.core;

import java.util.Locale;

/**
 * Configuration for validator behavior.
 */
public class ValidatorConfig {
    private final boolean failFast;
    private final String messagePrefix;
    private final Locale defaultLocale;

    private ValidatorConfig(Builder builder) {
        this.failFast = builder.failFast;
        this.messagePrefix = builder.messagePrefix;
        this.defaultLocale = builder.defaultLocale;
    }

    public boolean isFailFast() {
        return failFast;
    }

    public String getMessagePrefix() {
        return messagePrefix;
    }

    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    /**
     * Creates a new builder for ValidatorConfig.
     * 
     * @return a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean failFast = false;
        private String messagePrefix = "";
        private Locale defaultLocale = Locale.getDefault();

        public Builder failFast(boolean failFast) {
            this.failFast = failFast;
            return this;
        }

        public Builder messagePrefix(String messagePrefix) {
            this.messagePrefix = messagePrefix;
            return this;
        }

        public Builder defaultLocale(Locale defaultLocale) {
            this.defaultLocale = defaultLocale;
            return this;
        }

        public ValidatorConfig build() {
            return new ValidatorConfig(this);
        }
    }
}
