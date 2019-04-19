package com.evilduck.exception;

import javassist.NotFoundException;

public class UserNotInVoiceChannelException extends NotFoundException {

    public UserNotInVoiceChannelException(final String msg) {
        super(msg);
    }

}
