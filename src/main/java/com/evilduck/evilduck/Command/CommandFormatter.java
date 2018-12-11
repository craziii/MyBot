package com.evilduck.evilduck.Command;

import com.evilduck.evilduck.Exceptions.InvalidCommandException;
import org.springframework.integration.transformer.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class CommandFormatter implements Transformer {

    @Override
    public Message<?> transform(final Message<?> message) {
        String messagePayload = (String) message.getPayload();
        if (!isAValidCommand(messagePayload))
            throw new InvalidCommandException("Invalid Command");

        MessageBuilder<String> messageBuilder = MessageBuilder
                .withPayload(messagePayload)
                .setHeader("args", numberOfArgs(messagePayload));

        return message;  // TODO: FINISH ME!!!
    }

    private static boolean isAValidCommand(final String commandString) {
        return true;
    }

    private static int numberOfArgs(final String commandString) {
        return 1;
    }

}
