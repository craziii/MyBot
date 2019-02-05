package com.evilduck.Entity;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;

public class SessionEntity {

    @Id
    private String id;

    private final MemberEntity member;
    private final DateTime startTime;
    private DateTime lastUpdateTime;

    public SessionEntity(final MemberEntity member,
                         final DateTime startTime,
                         final DateTime lastUpdateTime) {
        this.id = member.getId();
        this.member = member;
        this.startTime = startTime;
        this.lastUpdateTime = lastUpdateTime;
    }


    public String getId() {
        return id;
    }

    public MemberEntity getMember() {
        return member;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public DateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(final DateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

}
