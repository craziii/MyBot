package com.evilduck.Command.AutoFireComponents;

import com.evilduck.Command.Interface.GenericCommand;
import com.evilduck.Command.Interface.IsACommand;
import com.evilduck.Repository.BannedPhraseRepository;
import com.evilduck.Util.CommandHelper;
import net.dv8tion.jda.core.entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@IsACommand(callable = false)
public class CheckBannedPhrase implements GenericCommand {

    private final static Logger LOGGER = LoggerFactory.getLogger(CheckBannedPhrase.class);

    private final BannedPhraseRepository bannedPhraseRepository;
    private final CommandHelper commandHelper;

    @Autowired
    public CheckBannedPhrase(final BannedPhraseRepository bannedPhraseRepository,
                             final CommandHelper commandHelper) {
        this.bannedPhraseRepository = bannedPhraseRepository;
        this.commandHelper = commandHelper;
    }

    @Override
    @ServiceActivator(inputChannel = "autoFireCommandChannel")
    public void execute(final org.springframework.messaging.Message<Message> message) {
        LOGGER.info("Checking message for banned phrases...");
        final String rawMessage = commandHelper.getArgsAsString(message.getPayload().getContentRaw(), 0);
        final List<String> matches = commandHelper.getArgs(message.getPayload()
                .getContentRaw()).stream()
                .filter(arg -> bannedPhraseRepository.findById(arg.toLowerCase()).isPresent())
                .collect(toList());
        if (matches.size() > 0 || bannedPhraseRepository.findById(rawMessage).isPresent()) {
            LOGGER.info("Message matched {} banned phrases", matches.size());
            message.getPayload().getTextChannel()
                    .sendMessage("Banned Phrase said my user: " +
                            message.getPayload().getAuthor().getAsMention())
                    .queue();
        } else {
            LOGGER.info("No Banned Phrases found in message");
        }
    }

}
