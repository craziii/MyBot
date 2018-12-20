package com.evilduck.evilduck.Command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.stereotype.Component;

@Component
public class CommandRouter extends Router {

    private CommandFormatter commandFormatter;

    @Autowired
    public CommandRouter(CommandFormatter commandFormatter) {
        this.commandFormatter = commandFormatter;
    }

    @Router
    public IntegrationFlow commandFlow() {
        return f -> f.transform(commandFormatter).channel();
    }


}
