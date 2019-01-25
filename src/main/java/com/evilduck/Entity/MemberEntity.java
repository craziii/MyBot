package com.evilduck.Entity;

import net.dv8tion.jda.core.entities.Member;
import org.springframework.data.annotation.Id;

public class MemberEntity {

    @Id
    private String id;

    private Member member;

    public MemberEntity(final Member member) {
        this.id = member.getUser().getId();
        this.member = member;
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

    @Override
    public String toString() {
        return "MemberEntity{" +
                "id='" + id + '\'' +
                ", member=" + member +
                '}';
    }

}
