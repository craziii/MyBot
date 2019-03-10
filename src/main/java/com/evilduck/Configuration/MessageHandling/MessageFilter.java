package com.evilduck.Configuration.MessageHandling;


import net.dv8tion.jda.core.entities.Message;
import org.springframework.integration.annotation.Filter;
import org.springframework.stereotype.Component;

@Component
public class MessageFilter {

    @Filter
    public boolean commandFilter(final org.springframework.messaging.Message<Message> message) {
        final net.dv8tion.jda.core.entities.Message payload = message.getPayload();
        return isNotMyself(payload) && isValidCommand(payload);

    }

    private static boolean isNotMyself(final Message payload) {
        return !payload.getAuthor().getDiscriminator().matches(payload.getJDA().getSelfUser().getDiscriminator());
    }

    private static boolean isValidCommand(final Message payload) {
        return payload.getContentRaw().charAt(0) == '!'; // TODO: MAKE THIS CONFIGURABLE
    }

}
