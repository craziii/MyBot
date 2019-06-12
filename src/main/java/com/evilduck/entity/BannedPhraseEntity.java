package com.evilduck.entity;

import org.springframework.data.annotation.Id;

public class BannedPhraseEntity {

    @Id
    private String id;
    private String phrase;

    public BannedPhraseEntity(final String id,
                              final String phrase) {
        this.id = id;
        this.phrase = phrase;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }
}
