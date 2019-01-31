package com.evilduck.Command.AutoFire;

import com.evilduck.Configuration.CommandConfiguration.GenericCommand;
import com.evilduck.Repository.BannedPhraseRepository;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BannedPhrase implements GenericCommand {

    private final BannedPhraseRepository bannedPhraseRepository;

    @Autowired
    public BannedPhrase(final BannedPhraseRepository bannedPhraseRepository) {
        this.bannedPhraseRepository = bannedPhraseRepository;
    }

    @Override
    public void execute(final org.springframework.messaging.Message<Message> message) {

        final Message payload = message.getPayload();

        if (isBannedPhrase(payload.getContentRaw())) {
            payload.getTextChannel().sendMessage("*MRS OBAMA,* ***GET DOWN!!!***").queue();
        }

    }

    private boolean isBannedPhrase(final String message) {
        return bannedPhraseRepository.findAll().stream().anyMatch(bannedPhrase ->
                message.matches("?i:" + bannedPhrase));
    }

    @Override
    public boolean hasPermissionToRun(final Member requestingMember) {
        return true;
    }

    @Override
    public void onSuccess(final Message message) {

    }

    @Override
    public void onFail(final Throwable throwable) {

    }

}
