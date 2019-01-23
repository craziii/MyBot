package com.evilduck.Entity;

import org.springframework.data.annotation.Id;

public class Member {

    @Id
    public final String id;
    public final String name;

    public Member(final String id,
                  final String name) {
        this.id = id;
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
