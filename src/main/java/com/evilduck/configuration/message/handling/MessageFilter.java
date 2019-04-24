package com.evilduck.configuration.message.handling;


import net.dv8tion.jda.core.entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.Filter;
import org.springframework.stereotype.Component;

@Component
public class MessageFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageFilter.class);

    @Filter
    public boolean commandFilter(final Message message) {
        if (isNotMyself(message)) return true;
        else {
            LOGGER.info("It's my own message! Ignoring.");
            return false;
        }
    }

    private boolean isValidCommand(final Message payload) {
        return payload.getContentRaw().length() > 0
                && (payload.getContentRaw().charAt(0) == '!'); // TODO: MAKE THIS CONFIGURABLE
    }

    private static boolean isNotMyself(final Message payload) {
        return !payload.getAuthor().getDiscriminator().matches(payload.getJDA().getSelfUser().getDiscriminator());
    }

}
