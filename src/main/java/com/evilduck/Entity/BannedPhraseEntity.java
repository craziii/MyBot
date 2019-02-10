package com.evilduck.Entity;

import org.springframework.data.annotation.Id;

public class BannedPhraseEntity {

    @Id
    private String id;

    public BannedPhraseEntity(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }


}
