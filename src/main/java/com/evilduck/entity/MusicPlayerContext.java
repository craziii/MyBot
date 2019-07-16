package com.evilduck.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class MusicPlayerContext {

    @Id
    private String id;
    private String textChannelId;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getTextChannelId() {
        return textChannelId;
    }

    public void setTextChannelId(final String textChannelId) {
        this.textChannelId = textChannelId;
    }

}
