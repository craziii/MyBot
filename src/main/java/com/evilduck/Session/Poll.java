package com.evilduck.Session;

import java.util.Map;

public class Poll {

    private final Map<String, Integer> options;

    public Poll(final Map<String, Integer> options) {
        this.options = options;
    }

    public Map<String, Integer> getOptions() {
        return options;
    }

    public void vote(final String key) {
        options.put(key, options.get(key) + 1);
    }

}
