package com.evilduck.Command;

import com.evilduck.Command.Interface.IsACommand;
import com.evilduck.Command.Interface.ManualCommand;
import com.evilduck.Entity.BannedPhraseEntity;
import com.evilduck.Repository.BannedPhraseRepository;
import com.evilduck.Util.CommandHelper;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static java.util.Arrays.asList;
import static net.dv8tion.jda.core.Permission.*;
import static org.apache.commons.lang3.StringUtils.isAlphanumeric;

@Component
@IsACommand(management = true)
public class BannedPhrase implements ManualCommand {

    private final BannedPhraseRepository bannedPhraseRepository;
    private final CommandHelper commandHelper;

    @Autowired
    public BannedPhrase(final BannedPhraseRepository bannedPhraseRepository,
                        final CommandHelper commandHelper) {
        this.bannedPhraseRepository = bannedPhraseRepository;
        this.commandHelper = commandHelper;
    }

    @Override
    public boolean hasPermissionToRun(final Member requestingMember) {
        return requestingMember.hasPermission(asList(KICK_MEMBERS, MANAGE_ROLES, MANAGE_SERVER));
    }

    @Override
    public void onSuccess(final Message message) {

    }

    @Override
    public void onFail(final Throwable throwable) {

    }

    @Override
    @ServiceActivator(inputChannel = "bannedPhraseChannel")
    public void execute(final org.springframework.messaging.Message<Message> message) throws IOException {
        final Member member = message.getPayload().getMember();
        final TextChannel textChannel = message.getPayload()
                .getTextChannel();
        if (hasPermissionToRun(member)) {
            final String contentRaw = message.getPayload().getContentRaw();
            final String action = commandHelper.getArgs(contentRaw).get(1);
            final String proposedBannedPhrase = commandHelper.getArgsAsString(contentRaw, 2)
                    .trim()
                    .toLowerCase();

            if (isAlphanumeric(proposedBannedPhrase.replace(" ", ""))) {
                performDatabaseAction(textChannel, action, proposedBannedPhrase);
            } else {
                textChannel.sendMessage("Banned phrases must be alphanumeric!").queue();
            }
        } else {
            textChannel.sendMessage("You do not have permission to edit banned phrases!").queue();
        }
    }

    private void performDatabaseAction(TextChannel textChannel, String action, String proposedBannedPhrase) {
        if (action.toUpperCase().matches("(I(NSERT)?)|(S(AVE)?)")) {
            bannedPhraseRepository.save(new BannedPhraseEntity(proposedBannedPhrase));
            textChannel.sendMessage("Banned phrase saved").queue();
        } else if (action.toUpperCase().matches("(D(ELETE)?)|(R(EMOVE)?)")) {
            bannedPhraseRepository.delete(new BannedPhraseEntity(proposedBannedPhrase));
            textChannel.sendMessage("Banned phrase removed").queue();
        }
    }
}
