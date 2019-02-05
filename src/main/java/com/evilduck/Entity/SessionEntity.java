package com.evilduck.Entity;

import org.springframework.data.annotation.Id;

public class SessionEntity {

    @Id
    private String id;

    private String savedDetail;

    public SessionEntity(final String id,
                         final String savedDetail) {
        this.id = id;
        this.savedDetail = savedDetail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSavedDetail() {
        return savedDetail;
    }

    public void setSavedDetail(String savedDetail) {
        this.savedDetail = savedDetail;
    }

    @Override
    public String toString() {
        return "SessionEntity{" +
                "id='" + id + '\'' +
                ", savedDetail='" + savedDetail + '\'' +
                '}';
    }

}
