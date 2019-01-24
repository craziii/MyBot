package com.evilduck.Entity;

import org.springframework.data.annotation.Id;

public class BitDickEntity {

    @Id
    private final String id;

    public BitDickEntity(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
