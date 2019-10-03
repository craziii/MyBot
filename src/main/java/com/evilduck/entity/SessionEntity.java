package com.evilduck.entity;

import org.springframework.data.annotation.Id;

public class SessionEntity {

    @Id
    private String authorId;
    private String nextStep;
    private String messageId;
    private String sessionType;
    private SessionDetail sessionDetail;

    public SessionEntity(final String authorId,
                         final String nextStep,
                         final String messageId,
                         final String sessionType, SessionDetail sessionDetail) {
        this.authorId = authorId;
        this.nextStep = nextStep;
        this.messageId = messageId;
        this.sessionType = sessionType;
        this.sessionDetail = sessionDetail;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(final String authorId) {
        this.authorId = authorId;
    }

    public String getNextStep() {
        return nextStep;
    }

    public void setNextStep(final String nextStep) {
        this.nextStep = nextStep;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(final String messageId) {

        this.messageId = messageId;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(final String sessionType) {
        this.sessionType = sessionType;
    }

    public SessionDetail getSessionDetail() {
        return sessionDetail;
    }

    public void setSessionDetail(final SessionDetail sessionDetail) {
        this.sessionDetail = sessionDetail;
    }
}
