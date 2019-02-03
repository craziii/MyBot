package com.evilduck.Configuration.MessageHandling;


import net.dv8tion.jda.core.entities.Message;
import org.springframework.integration.annotation.Filter;
import org.springframework.stereotype.Component;

@Component
public class CommandFilter {

    @Filter
    public boolean commandFilter(final org.springframework.messaging.Message<Message> message) {

        final net.dv8tion.jda.core.entities.Message payload = message.getPayload();
        return isNotMyself(payload);

    }

    private static boolean isNotMyself(Message payload) {
        return !payload.getAuthor().getDiscriminator().matches(payload.getJDA().getSelfUser().getDiscriminator());
    }

}
