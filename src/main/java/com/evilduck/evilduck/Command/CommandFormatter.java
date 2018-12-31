package com.evilduck.evilduck.Command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static org.springframework.messaging.support.MessageBuilder.withPayload;

@Component
public class CommandFormatter {

    private final static Logger LOGGER = LoggerFactory.getLogger(CommandFormatter.class);

    @Transformer
    public Message<net.dv8tion.jda.core.entities.Message> transform(final Message<net.dv8tion.jda.core.entities.Message> message) {
        LOGGER.info("Transforming message {}", message.getPayload().getId());
        return withPayload(message.getPayload()).setHeader("args", getCommandArgs(message.getPayload().getContentRaw())).build();
    }

    private static int getCommandArgs(final String commandString) {
        return commandString.replace("!", "").split(" ").length;
    }

}
