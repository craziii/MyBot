package com.evilduck.Command;

import com.evilduck.Configuration.MessageHandling.GenericCommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.PermissionException;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.util.List;

import static net.dv8tion.jda.core.Permission.KICK_MEMBERS;

@Component
public class Kick implements GenericCommand {

    @Override
    @ServiceActivator(inputChannel = "kickChannel")
    public void execute(final org.springframework.messaging.Message<Message> message) {

        if (!hasPermissionToRun(message.getPayload().getMember()))
            throw new PermissionException(KICK_MEMBERS.getName());

        final TextChannel channel = message.getPayload().getTextChannel();
        final List<User> mentionedUsers = message.getPayload().getMentionedUsers();
        final Guild guild = message.getPayload().getGuild();
        final Member selfMember = guild.getSelfMember();

        verifyCommandToRun(channel, mentionedUsers, selfMember);


        for (final User mentionedUser : mentionedUsers) {
            final Member mentionedMember = guild.getMember(mentionedUser);
            final String effectiveName = mentionedMember.getEffectiveName();

            if (!selfMember.canInteract(mentionedMember)) {
                channel.sendMessage("I do not have permission to interact with member ")
                        .append(effectiveName)
                        .queue();
                continue;
            }

            guild.getController().kick(mentionedMember).queue(success -> {
                        mentionedUser.openPrivateChannel().queue(privateChannel ->
                                privateChannel
                                        .sendMessage("Hahaha, you've been kicked by ***ME!!!***")
                                        .queue());
                        channel.sendMessage("Member ")
                                .append(effectiveName)
                                .append(" kicked!").queue();

                    },
                    error -> handleKickError(channel, mentionedMember, error)
            );
        }
    }

    @Override
    public boolean hasPermissionToRun(final Member requestingMember) {
        return requestingMember.hasPermission(KICK_MEMBERS);
    }


    private void verifyCommandToRun(final TextChannel channel,
                                    final List<User> mentionedUsers,
                                    final Member selfMember) {
        if (!selfMember.hasPermission(KICK_MEMBERS))
            channel.sendMessage("I do not have permission to kick members").queue();
        else if (mentionedUsers.size() == 0)
            channel.sendMessage("Please mention the user you want to kick").queue();
        else if (mentionedUsers.size() > 1)
            channel.sendMessage("Please only specify one user to kick at a time").queue();
    }

    @Override
    public void onSuccess(final Message message) {

    }

    @Override
    public void onFail(final Throwable throwable) {

    }

    private static void handleKickError(final TextChannel channel,
                                        final Member mentionedMember,
                                        final Throwable error) {
        if (error instanceof PermissionException) {
            final PermissionException exception = ((PermissionException) error);
            final Permission missingPermission = exception.getPermission();

            channel.sendMessage("Permission Exception when kicking user ")
                    .append(mentionedMember.getEffectiveName())
                    .append(", missing permission: ")
                    .append(missingPermission.getName())
                    .append(", error message: ")
                    .append(error.getMessage())
                    .queue();

        } else {
            channel.sendMessage("Unknown error whilst kicking user ")
                    .append(mentionedMember.getEffectiveName())
                    .queue();
        }
    }
}
