package com.evilduck.evilduck.Command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public class CommandRouter {

    private final CommandFormatter commandFormatter;
    private final MessageChannel pingChannel;

    @Autowired
    public CommandRouter(final CommandFormatter commandFormatter,
                         final MessageChannel pingChannel) {
        this.commandFormatter = commandFormatter;
        this.pingChannel = pingChannel;
    }

    @Router
    public IntegrationFlow commandFlow() {
        return f -> f.transform(commandFormatter)
                .channel(pingChannel);
    }


}
