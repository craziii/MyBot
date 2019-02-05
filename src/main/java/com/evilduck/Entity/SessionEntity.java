package com.evilduck.Entity;

import net.dv8tion.jda.core.entities.Member;
import org.springframework.data.annotation.Id;

public class SessionEntity {

    @Id
    private String id;

    private Member member;
    private String savedDetail;

    public SessionEntity(final Member member,
                         final String savedDetail) {
        this.id = member.getUser().getId();
        this.member = member;
        this.savedDetail = savedDetail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Object getSavedDetail() {
        return savedDetail;
    }

    public void setSavedDetail(String savedDetail) {
        this.savedDetail = savedDetail;
    }

    @Override
    public String toString() {
        return "SessionEntity{" +
                "id='" + id + '\'' +
                ", member=" + member +
                ", savedDetail=" + savedDetail +
                '}';
    }
}
