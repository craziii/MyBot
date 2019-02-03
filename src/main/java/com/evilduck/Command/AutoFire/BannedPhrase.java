package com.evilduck.Command.AutoFire;

import com.evilduck.Configuration.MessageHandling.GenericCommand;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.stereotype.Component;

@Component
public class BannedPhrase implements GenericCommand {

//    private final BannedPhraseRepository bannedPhraseRepository;
//
//    @Autowired
//    public BannedPhrase(final BannedPhraseRepository bannedPhraseRepository) {
//        this.bannedPhraseRepository = bannedPhraseRepository;
//    }

    @Override
    public void execute(final org.springframework.messaging.Message<Message> message) {

        final Message payload = message.getPayload();

//        if (isBannedPhrase(payload.getContentRaw())) {
//            payload.getTextChannel().sendMessage("*MRS OBAMA,* ***GET DOWN!!!***").queue();
//        }

    }

    private boolean isBannedPhrase(final String message) {
//        return bannedPhraseRepository.findAll().stream().anyMatch(bannedPhrase ->
//                message.matches("?i:" + bannedPhrase));
        return false;
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
