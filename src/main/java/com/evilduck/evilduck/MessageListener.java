package com.evilduck.evilduck;

import com.evilduck.evilduck.Configuration.CommandConfiguration.CommandGateway;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.springframework.integration.support.MessageBuilder.withPayload;

@Component
public class MessageListener extends ListenerAdapter {

    private final CommandGateway commandGateway;

    @Autowired
    public MessageListener(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        commandGateway.processCommand(withPayload(event.getMessage()).build());
    }

}
