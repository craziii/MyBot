package com.evilduck.command.autofirecomponents;

import com.evilduck.command.interfaces.GenericCommand;
import com.evilduck.command.interfaces.IsACommand;
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
        if (matchesBannedPhrase(message.getContentRaw())) {
            message.getTextChannel()
                    .sendMessage("Banned Phrase said by user: " +
                            message.getAuthor().getAsMention())
                    .queue();
        } else {
            LOGGER.info("No Banned Phrases found in message");
        }
    }

    private boolean matchesBannedPhrase(final String rawContent) {
        LOGGER.info("Checking message for banned phrases...");
        final List<String> matches = commandHelper.getArgs(rawContent, true).stream()
                .filter(arg -> bannedPhraseRepository.findAll().stream().anyMatch(bannedPhraseEntity -> arg.toLowerCase().contains(bannedPhraseEntity.getId())))
                .collect(toList());
        LOGGER.info("Message matched {} banned phrases", matches.size());
        return (matches.size() > 0 || bannedPhraseRepository.findById(rawContent).isPresent());
    }

}
