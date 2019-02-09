package com.evilduck.Command.AutoFireComponents;

import com.evilduck.Command.Interface.AutoFire;
import com.evilduck.Repository.BannedPhraseRepository;
import com.evilduck.Util.CommandHelper;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class BannedPhrase implements AutoFire {

    private final BannedPhraseRepository bannedPhraseRepository;
    private final CommandHelper commandHelper;

    @Autowired
    public BannedPhrase(final BannedPhraseRepository bannedPhraseRepository,
                        final CommandHelper commandHelper) {
        this.bannedPhraseRepository = bannedPhraseRepository;
        this.commandHelper = commandHelper;
    }

    @Override
    public boolean execute(org.springframework.messaging.Message<Message> message) {

        final List<String> matches = commandHelper.getArgs(message.getPayload()
                .getContentRaw()).stream()
                .filter(arg -> bannedPhraseRepository.findByIdLike(arg) != null)
                .collect(toList());
        if (matches.size() > 0) {
            message.getPayload().getTextChannel()
                    .sendMessage("Banned Phrase said my user: " +
                            message.getPayload().getAuthor().getAsMention())
                    .queue();
            return true;
        }
        return false;
    }
}
