package com.evilduck.command;

import com.evilduck.command.standards.IsACommand;
import com.evilduck.command.standards.PrivateCommand;
import com.evilduck.command.standards.UnstableCommand;
import com.evilduck.entity.BannedPhraseEntity;
import com.evilduck.repository.BannedPhraseRepository;
import com.evilduck.util.CommandHelper;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import static java.util.Arrays.asList;
import static net.dv8tion.jda.core.Permission.KICK_MEMBERS;
import static net.dv8tion.jda.core.Permission.MANAGE_ROLES;
import static net.dv8tion.jda.core.Permission.MANAGE_SERVER;
import static org.apache.commons.lang3.StringUtils.isAlphanumeric;

@Component
@IsACommand(
        management = true,
        description = "Allows adding and removing banned phrases for this server",
        aliases = {"bp"}
)
public class BannedPhrase implements PrivateCommand, UnstableCommand {

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
    public void execute(final Message message) {
        final Member member = message.getMember();
        final TextChannel textChannel = message
                .getTextChannel();
        if (hasPermissionToRun(member)) {
            final String contentRaw = message.getContentRaw();
            final String action = commandHelper.getArgs(contentRaw).get(0);
            final String proposedBannedPhrase = commandHelper.getArgsAsString(contentRaw, 1)
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
            bannedPhraseRepository.save(new BannedPhraseEntity("test", proposedBannedPhrase));
            textChannel.sendMessage("Banned phrase saved").queue();
        } else if (action.toUpperCase().matches("(D(ELETE)?)|(R(EMOVE)?)")) {
            bannedPhraseRepository.delete(new BannedPhraseEntity("test", proposedBannedPhrase));
            textChannel.sendMessage("Banned phrase removed").queue();
        }
    }
}
