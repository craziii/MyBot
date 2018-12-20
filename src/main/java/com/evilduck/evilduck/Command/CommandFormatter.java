package com.evilduck.evilduck.Command;

import org.springframework.integration.transformer.Transformer;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static java.util.Arrays.asList;
import static org.springframework.messaging.support.MessageBuilder.withPayload;

@Component
public class CommandFormatter implements Transformer {

    @Override
    public Message<?> transform(final Message<?> message) {
        final String messagePayload = (String) message.getPayload();
        final String[] commandArgs = getCommandArgs(messagePayload);
        final Command command = new Command(commandArgs[0], asList(messagePayload.replaceFirst("!.* ", "").split(" ")));
        return withPayload(command).build();
    }

    private static String[] getCommandArgs(final String commandString) {
        return commandString.replace("!", "").split(" ");
    }

}
