package com.evilduck.evilduck.Command;

public enum CommandType {

    PING("ping");

    private String commandText;

    CommandType(final String commandText) {
        this.commandText = commandText;
    }

    public String getCommandText() {
        return commandText;
    }
}
