package com.evilduck.command.autofirecomponents;

import com.evilduck.command.standards.GenericCommand;
import com.evilduck.command.standards.IsACommand;
import com.evilduck.repository.BannedPhraseRepository;
import com.evilduck.util.CommandHelper;
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
    public void execute(final Message message) {
        LOGGER.info("Checking message for banned phrases...");
        final String rawMessage = commandHelper.getArgsAsString(message.getContentRaw(), 0);
        final List<String> matches = commandHelper.getArgs(message.getContentRaw()).stream()
                .filter(arg -> bannedPhraseRepository.findById(arg.toLowerCase()).isPresent())
                .collect(toList());
        if (matches.size() > 0 || bannedPhraseRepository.findById(rawMessage).isPresent()) {
            LOGGER.info("Message matched {} banned phrases", matches.size());
            message.getTextChannel()
                    .sendMessage("Banned Phrase said my user: " +
                            message.getAuthor().getAsMention())
                    .queue();
        } else {
            LOGGER.info("No Banned Phrases found in message");
        }
    }

}
