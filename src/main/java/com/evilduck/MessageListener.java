package com.evilduck;

import com.evilduck.configuration.message.handling.MessageGateway;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.springframework.integration.support.MessageBuilder.withPayload;

@Component
public class MessageListener extends ListenerAdapter {

    private final MessageGateway messageGateway;

    @Autowired
    public MessageListener(MessageGateway messageGateway) {
        this.messageGateway = messageGateway;
    }

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        messageGateway.processCommand(withPayload(event.getMessage()).build());
    }

}
