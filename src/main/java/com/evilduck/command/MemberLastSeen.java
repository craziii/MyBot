package com.evilduck.command;

import com.evilduck.command.interfaces.IsACommand;
import com.evilduck.command.interfaces.PublicCommand;
import com.evilduck.util.AudioPlayerSupport;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
@IsACommand(description = "Shows when the last message was sent by a specified user", tutorial = "Use !memberlastseen or !lastseen",
            aliases = {"memberlastseen", "lastseen"})
public class MemberLastSeen implements PublicCommand {

    private final AudioPlayerSupport audioPlayerSupport;

    public MemberLastSeen(final AudioPlayerSupport audioPlayerSupport) {
        this.audioPlayerSupport = audioPlayerSupport;
    }

    @Override
    @ServiceActivator(inputChannel = "memberLastSeenChannel")
    public void execute(final Message message) {
        final List<User> mentionedMembers = message.getMentionedUsers();
        final User user;
        final TextChannel textChannel = message.getTextChannel();
        if (mentionedMembers.size() > 0) {
            user = mentionedMembers.get(0);
        } else {
            textChannel.sendMessage("You must specify a user to search for").queue();
            return;
        }
        textChannel.sendMessage("Searching for user, this can take a while...").queue();
        final Optional<Message> lastSentMessageForUser = textChannel.getIterableHistory()
                .cache(false)
                .stream().filter(m -> m.getAuthor().getId().equals(user.getId())).findFirst();
        if (lastSentMessageForUser.isPresent()) {
            final OffsetDateTime creationTime = lastSentMessageForUser.get().getCreationTime();
            textChannel.sendMessage(user.getName() +
                    " was last seen at ***\"" +
                    creationTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy '@' HH:mm:ss.SSSZ")) +
                    "\"***, they sent the message ***\"" +
                    lastSentMessageForUser.get().getContentRaw() +
                    "\"***").queue();
        } else {
            textChannel.sendMessage("I could not find a message for that user").queue();
        }

    }

}
