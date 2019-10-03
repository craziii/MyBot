package com.evilduck.session;

import org.springframework.data.annotation.Id;

import java.util.Map;

public class PollSession {

    @Id
    private String memberId;
    private String messageId;
    private Map<String, Integer> options;


    public PollSession(final String memberId,
                       final String messageId,
                       final Map<String, Integer> options) {
        this.memberId = memberId;
        this.messageId = messageId;
        this.options = options;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(final String messageId) {
        this.messageId = messageId;
    }

    public Map<String, Integer> getOptions() {
        return options;
    }

    public void vote(final String key) {
        options.put(key, options.get(key) + 1);
    }

}
