package com.evilduck.evilduck.Command;

import java.util.List;

public class Command {

    private final String command;
    private final List<String> args;

    public Command(final String command,
                   final List<String> args) {
        this.command = command;
        this.args = args;
    }

    public String getCommand() {
        return command;
    }

    public List<String> getArgs() {
        return args;
    }

}
