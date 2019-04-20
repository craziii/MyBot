package com.evilduck.session;

import org.springframework.data.annotation.Id;

/*
 *
 * A session saved for a particular user for a Command class.
 *
 * Allows the app to *remember* that a user is communicating
 * with a particular Command.
 *
 * */
public class ResponseSession {

    @Id
    private String memberId;
    private int progress;
    private String commandName;

    public ResponseSession(final String memberId,
                           final int progress,
                           final String commandName) {
        this.memberId = memberId;
        this.progress = progress;
        this.commandName = commandName;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(final String memberId) {
        this.memberId = memberId;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(final int progress) {
        this.progress = progress;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(final String commandName) {
        this.commandName = commandName;
    }
}
