package com.evilduck.Configuration.Joel;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JoelTest {

    @Test
    public void shouldSayLol() {
        final Joel joel = new Joel();
        assertEquals(joel.getLol(), "lol");
    }

}