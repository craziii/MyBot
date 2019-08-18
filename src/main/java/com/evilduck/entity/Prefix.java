package com.evilduck.entity;

public class Prefix {

    private final String prefix;

    public Prefix(final String prefix) {
        this.prefix = prefix;
    }

    public boolean matches(final String rawMessage) {
        return rawMessage.matches("^" + prefix);
    }

    public String getPrefix() {
        return prefix;
    }
}
