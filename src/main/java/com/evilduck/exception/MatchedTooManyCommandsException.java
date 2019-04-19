package com.evilduck.exception;

public class MatchedTooManyCommandsException extends RuntimeException {

    public MatchedTooManyCommandsException(final String message) {
        super(message);
    }

}
