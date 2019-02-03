package com.evilduck.Exception;

public class MatchedTooManyCommandsException extends RuntimeException {

    public MatchedTooManyCommandsException(final String message) {
        super(message);
    }

}
