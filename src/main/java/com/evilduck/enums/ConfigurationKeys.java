package com.evilduck.enums;

public enum ConfigurationKeys {

    PREFIX("prefix");

    private String key;

    ConfigurationKeys(final String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }
}
