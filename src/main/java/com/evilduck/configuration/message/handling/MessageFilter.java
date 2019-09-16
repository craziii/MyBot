package com.evilduck.configuration.message.handling;


import com.evilduck.repository.SimpleConfigurationRepository;
import net.dv8tion.jda.core.entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.Filter;
import org.springframework.stereotype.Component;

@Component
public class MessageFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageFilter.class);

    private final SimpleConfigurationRepository configurationRepository;

    public MessageFilter(final SimpleConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    @Filter
    public boolean commandFilter(final Message message) {
        if (isMyself(message)) {
            LOGGER.info("Message from myself, ignoring");
            return false;
        } else if (isBot(message)) {
            LOGGER.info("Message from another bot, ignoring");
            return false;
        } else if (!isValidCommand(message)) {
            LOGGER.info("Invalid command message, ignoring");
            return false;
        }
        return true;
    }

    private boolean isValidCommand(final Message payload) {
        final String contentRaw = payload.getContentRaw();
        return (contentRaw.length() > 0)
                && (contentRaw.startsWith(configurationRepository.getPrefix())
                || payload.getMentionedUsers().contains(payload.getJDA().getSelfUser()));
    }

    private static boolean isBot(final Message payload) {
        return payload.getAuthor().isBot();
    }

    private static boolean isMyself(final Message payload) {
        return payload.getAuthor().getDiscriminator().matches(payload.getJDA().getSelfUser().getDiscriminator());
    }

}
