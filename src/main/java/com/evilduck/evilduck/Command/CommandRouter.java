package com.evilduck.evilduck.Command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@MessageEndpoint
public class CommandRouter {

    private final CommandFormatter commandFormatter;
    private final MessageChannel commandInputChannel;
    private final MessageChannel pingChannel;

    @Autowired
    public CommandRouter(final CommandFormatter commandFormatter,
                         final MessageChannel commandInputChannel,
                         final MessageChannel pingChannel) {
        this.commandFormatter = commandFormatter;
        this.commandInputChannel = commandInputChannel;
        this.pingChannel = pingChannel;
    }

    @Router(inputChannel = "commandInputChannel", defaultOutputChannel = "pingChannel")
    public void commandFlow(final Message<String> message) {
        System.out.println("Received!");
    }


}
