package com.evilduck.Entity;

import org.springframework.data.annotation.Id;

public class Member {

    @Id public String id;
    public String name;

    public Member() {
    }

    public Member(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
