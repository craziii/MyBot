package com.evilduck.Entity;

import net.dv8tion.jda.core.entities.Member;
import org.springframework.data.annotation.Id;

public class MemberEntity {

    @Id
    private String id;

    private Member member;

    public MemberEntity(final Member member) {
        this.member = member;
        this.id = member.getUser().getId();
    }

    public String getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }
}
