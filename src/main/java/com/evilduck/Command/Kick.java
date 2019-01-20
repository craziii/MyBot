package com.evilduck.Command;

import com.evilduck.Configuration.CommandConfiguration.GenericCommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.exceptions.PermissionException;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.util.List;

import static net.dv8tion.jda.core.Permission.KICK_MEMBERS;

@Component
public class Kick implements GenericCommand {

    @Override
    @ServiceActivator(inputChannel = "kickChannel")
    public void execute(org.springframework.messaging.Message<Message> message) {

        final TextChannel channel = message.getPayload().getTextChannel();
        final List<User> mentionedUsers = message.getPayload().getMentionedUsers();
        final Guild guild = message.getPayload().getGuild();
        final Member selfMember = guild.getSelfMember();

        if (!selfMember.hasPermission(KICK_MEMBERS))
            channel.sendMessage("I do not have permission to kick members").queue();
        else if (mentionedUsers.size() == 0)
            channel.sendMessage("Please mention the user you want to kick").queue();
        else if (mentionedUsers.size() > 1)
            channel.sendMessage("Please only specify one user to kick at a time").queue();


        for (final User mentionedUser : mentionedUsers) {
            final Member mentionedMember = guild.getMember(mentionedUser);
            final String effectiveName = mentionedMember.getEffectiveName();

            if (!selfMember.canInteract(mentionedMember)) {
                channel.sendMessage("I do not have permission to interact with member ")
                        .append(effectiveName)
                        .queue();
                continue;
            }

            guild.getController()
                    .kick(mentionedMember).queue(success -> {
                        mentionedUser.openPrivateChannel().queue(privateChannel ->
                                privateChannel
                                        .sendMessage("Hahaha, you've been kicked by ***ME!!!***")
                                        .queue());
                        channel
                                .sendMessage("Member ")
                                .append(effectiveName)
                                .append(" kicked!").queue();

                    },
                    error -> handleKickError(channel, mentionedMember, error)
            );
        }
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
