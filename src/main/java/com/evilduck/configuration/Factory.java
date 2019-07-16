package com.evilduck.configuration;

public interface Factory<T> {

    <T> T create();
}
