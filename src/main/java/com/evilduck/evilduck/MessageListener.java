package com.evilduck.evilduck;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import static org.springframework.integration.support.MessageBuilder.withPayload;

@Component
public class MessageListener extends ListenerAdapter {

    private final MessageChannel commandInputChannel;

    @Autowired
    public MessageListener(MessageChannel commandInputChannel) {
        this.commandInputChannel = commandInputChannel;
    }

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        System.out.printf("Received message from author: %s : %s",
                event.getAuthor().getName(),
                event.getMessage().getContentDisplay());
        final String rawCommand = event.getMessage().getContentRaw();
        if (rawCommand.equals("!ping"))
            event.getChannel().sendMessage("Lol Isaac is the big gay! 8======D").queue();
        commandInputChannel.send(withPayload(rawCommand).build());
    }

}
