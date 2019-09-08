package com.evilduck.entity;

import org.springframework.data.annotation.Id;

public class Configuration {

    @Id
    private final String key;

    private final String value;

    public Configuration(final String key,
                         final String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
