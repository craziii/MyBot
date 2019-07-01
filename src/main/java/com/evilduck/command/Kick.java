package com.evilduck.command;

import com.evilduck.command.interfaces.IsACommand;
import com.evilduck.command.interfaces.PrivateCommand;
import com.evilduck.command.interfaces.UnstableCommand;
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
@IsACommand(aliases = {"k"})
public class Kick implements PrivateCommand, UnstableCommand {

    @Override
    @ServiceActivator(inputChannel = "kickChannel")
    public void execute(final Message message) {

        if (!hasPermissionToRun(message.getMember()))
            throw new PermissionException(KICK_MEMBERS.getName());

        final TextChannel channel = message.getTextChannel();
        final List<User> mentionedUsers = message.getMentionedUsers();
        final Guild guild = message.getGuild();
        final Member selfMember = guild.getSelfMember();

        if (!verifyCommandToRun(channel, message.getMentionedMembers(), selfMember)) return;

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


    private boolean verifyCommandToRun(final TextChannel channel,
                                       final List<Member> mentionedMembers,
                                       final Member selfMember) {
        if (mentionedMembers.stream().anyMatch(member -> member.getUser().getName().contains("DuckChan|EvilDuck")))
            channel.sendMessage("I am sworn to protect my creator!").queue();
        else if (!selfMember.hasPermission(KICK_MEMBERS))
            channel.sendMessage("I do not have permission to kick members").queue();
        else if (mentionedMembers.size() == 0)
            channel.sendMessage("Please mention the user you want to kick").queue();
        else if (mentionedMembers.size() > 1)
            channel.sendMessage("Please only specify one user to kick at a time").queue();
        else if (mentionedMembers.stream().anyMatch(member -> !selfMember.canInteract(member)))
            channel.sendMessage("You cannot interact with this member").queue();

        else
            return true;

        return false;
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

            channel.sendMessage("Permission exception when kicking user ")
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
