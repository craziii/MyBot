package com.evilduck.command;

import com.evilduck.command.interfaces.IsACommand;
import com.evilduck.command.interfaces.PublicCommand;
import com.evilduck.util.AudioPlayerSupport;
import com.evilduck.util.CommandHelper;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static java.awt.Color.YELLOW;
import static java.time.format.DateTimeFormatter.ofPattern;
import static net.dv8tion.jda.core.Permission.MESSAGE_HISTORY;

@Component
@IsACommand(description = "Shows when the last message was sent by a specified user", tutorial = "Use !memberlastseen or !lastseen",
            aliases = {"memberlastseen", "lastseen"})
public class MemberLastSeen implements PublicCommand {

    private final CommandHelper commandHelper;
    private final AudioPlayerSupport audioPlayerSupport;

    @Autowired
    public MemberLastSeen(final CommandHelper commandHelper,
                          final AudioPlayerSupport audioPlayerSupport) {
        this.commandHelper = commandHelper;
        this.audioPlayerSupport = audioPlayerSupport;
    }

    @Override
    @ServiceActivator(inputChannel = "memberLastSeenChannel")
    public void execute(final Message message) {
        final List<User> mentionedMembers = message.getMentionedUsers();
        final User user;
        final TextChannel textChannel = message.getTextChannel();
        final List<String> args = commandHelper.getArgs(message.getContentRaw());
        final Guild guild = message.getGuild();
        if (mentionedMembers.size() > 0) {
            user = mentionedMembers.get(0);
        } else if (args.size() > 0) {
            user = guild.getMemberById(args.get(0)).getUser();
        } else {
            textChannel.sendMessage("You must specify a user to search for").queue();
            return;
        }
        final Message searchFeedback = textChannel.sendMessage("Searching for user, this can take a while...").complete();
        final UserSearchResult searchResult = new UserSearchResult();
        final List<TextChannel> textChannels = guild.getTextChannels();
        textChannels.forEach(channel -> {
            final String searchingChannelName = channel.getName();
            final double progress = (((double) textChannels.indexOf(channel)) / textChannels.size()) * 100;
            searchFeedback.editMessage("Searching *" +
                    searchingChannelName +
                    "* | Progress **" +
                    String.format("%2f", progress) +
                    "%**").queue();
            if (guild.getSelfMember().hasPermission(channel, MESSAGE_HISTORY)) {
                final Optional<Message> lastSentMessageForUser = findUserInTextChannel(user, channel);
                final Message lastFoundUserMessage = searchResult.getMessage();
                if (lastSentMessageForUser.isPresent() &&
                        (lastFoundUserMessage == null || lastFoundUserMessage.getCreationTime()
                                .isBefore(lastSentMessageForUser.get().getCreationTime()))) {
                    searchResult.setMessage(lastSentMessageForUser.get());
                    searchResult.setTextChannel(channel);
                }
            } else {
                searchFeedback.editMessage("I lack permission to access history for " + searchingChannelName).queue();
            }
        });
        final Message lastUserMessage = searchResult.getMessage();
        if (lastUserMessage == null) {
            searchFeedback.editMessage("I could not find a message for that user").queue();
        } else {
            searchFeedback.editMessage("I found a message for that user!").queue();
            final OffsetDateTime creationTime = lastUserMessage.getCreationTime();
            final EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("User Last Seen");
            embedBuilder.setColor(YELLOW);
            embedBuilder.addField("User", user.getName(), true);
            embedBuilder.addField("Time", creationTime.format(ofPattern("dd/MM/yyyy '@' HH:mm:ss.SSSZ")), true);
            embedBuilder.addField("Text Channel", searchResult.getTextChannel().getName(), true);
            embedBuilder.addField("Message", lastUserMessage.getContentRaw(), false);
            textChannel.sendMessage(embedBuilder.build()).queue();
        }

    }

    private Optional<Message> findUserInTextChannel(User user, TextChannel textChannel) {
        return textChannel.getIterableHistory()
                .cache(false)
                .stream().filter(m -> m.getAuthor().getId().equals(user.getId())).findFirst();
    }

    private class UserSearchResult {

        private Message message;
        private TextChannel textChannel;

        private UserSearchResult(final Message message,
                                 final TextChannel textChannel) {
            this.message = message;
            this.textChannel = textChannel;
        }

        public UserSearchResult() {

        }

        public Message getMessage() {
            return message;
        }

        public TextChannel getTextChannel() {
            return textChannel;
        }

        public void setMessage(final Message message) {
            this.message = message;
        }

        public void setTextChannel(final TextChannel textChannel) {
            this.textChannel = textChannel;
        }

    }

}
