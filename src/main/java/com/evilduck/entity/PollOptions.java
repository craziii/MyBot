package com.evilduck.entity;

import java.util.List;

public class PollOptions implements SessionDetail {

    private long timeToLive;
    private String question;
    private List<String> options;

    public PollOptions(final long timeToLive,
                       final String question,
                       final List<String> options) {
        this.timeToLive = timeToLive;
        this.question = question;
        this.options = options;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(final List<String> options) {
        this.options = options;
    }

    public long getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(final long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
