package com.evilduck.evilduck.Exceptions;

import java.io.IOException;

public class InvalidCommandException extends IOException {

    // TODO: This exception is redundant if no unique behaviour found.
    public InvalidCommandException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public synchronized Throwable getCause() {
        return super.getCause();
    }
}
