package com.evilduck.command;

import com.evilduck.command.standards.IsACommand;
import com.evilduck.command.standards.PublicCommand;
import com.evilduck.repository.PollSessionRepository;
import com.evilduck.repository.ResponseSessionRepository;
import com.evilduck.session.PollSession;
import com.evilduck.session.ResponseSession;
import com.evilduck.util.CommandHelper;
import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Long.parseLong;

@Component
@IsACommand
public class Poll implements PublicCommand {

    private static final HashMap NUMBER_EMOJIS = new HashMap<Integer, String>() {{
        put(0, ":zero:");
        put(1, ":one:");
        put(2, ":two:");
        put(3, ":three:");
        put(4, ":four:");
        put(5, ":five:");
        put(6, ":six:");
        put(7, ":seven:");
        put(8, ":eight:");
        put(9, ":nine:");
    }};

    private final ResponseSessionRepository responseSessionRepository;
    private final PollSessionRepository pollSessionRepository;
    private final CommandHelper commandHelper;

    public Poll(final ResponseSessionRepository responseSessionRepository,
                final PollSessionRepository pollSessionRepository,
                final CommandHelper commandHelper) {
        this.responseSessionRepository = responseSessionRepository;
        this.pollSessionRepository = pollSessionRepository;
        this.commandHelper = commandHelper;
    }

    @Override
    @ServiceActivator(inputChannel = "pollChannel")
    public void execute(final Message message) {
        final String userId = message.getAuthor().getDiscriminator();
        final Optional<ResponseSession> session = responseSessionRepository.findById(userId);
        final Optional<PollSession> userPoll = pollSessionRepository.findById(userId);

        if (session.isPresent() && userPoll.isPresent()) startPoll(message, userId, userPoll.get());
        else if (userPoll.isPresent()) existingPollActions(message, userId);
        else finalizePollCreation(message, userId);
    }

    private void existingPollActions(final Message message,
                                     final String userId) {
        final List<String> args = commandHelper.getArgs(message.getContentRaw());

        if (args.size() > 0 && args.get(0).toUpperCase().matches("(REMOVE|DELETE|STOP|CANCEL)")) {
            deletePoll(message, userId);
        } else {
            // Reckon I could format this better, but I don't know how....
            message.getTextChannel()
                    .sendMessage("You already have a poll, " +
                            "please send `poll cancel` to cancel " +
                            "your poll before starting a new one").queue();
        }
    }

    private static boolean responseIsValid(final String message) {
        return message.matches("[0-9]*");
    }

    private void displayPollInTextChannel(final Message message,
                                          final PollSession newPoll) {
        final PollSession actualPoll = pollSessionRepository.findById(message.getAuthor().getDiscriminator()).orElse(null);
        final EmbedBuilder embedBuilder = new EmbedBuilder().setTitle(message.getAuthor().getName() + "'s Poll");
        final Map<String, Integer> options = newPoll.getOptions();
        final List<String> optionsKeys = new ArrayList<>(options.keySet());
        for (int i = 0; i < options.size(); i++) embedBuilder.addField(String.valueOf(i), optionsKeys.get(i), true);
        embedBuilder.setDescription("Vote via the reaction icons below");

        message.getTextChannel().sendMessage(embedBuilder.build()).queue(success -> {
            final int numOfOptions = actualPoll.getOptions().size();
            for (int i = 0; i < numOfOptions; i++)
                success.addReaction(EmojiParser.parseToUnicode((String) NUMBER_EMOJIS.get(i))).queue();
            actualPoll.setMessageId(success.getId());
            pollSessionRepository.save(actualPoll);
        });
    }

    private PollSession createNewPoll(final String requesterId,
                                      final Message message) {
        final List<String> args = commandHelper.getArgs(message.getContentRaw());
        final Map<String, Integer> pollOptions = new LinkedHashMap<>();
        for (int i = 0; i < args.size(); i++) pollOptions.put(args.get(i), i);

        return new PollSession(requesterId, "0", pollOptions);
    }

    private void finalizePollCreation(final Message message,
                                      final String requesterId) {
        final PollSession newPoll = createNewPoll(requesterId, message);
        pollSessionRepository.save(newPoll);
        message.getTextChannel().sendMessage("How many minutes should this poll last?").queue();

        final ResponseSession session = new ResponseSession(requesterId, 0, "poll");
        responseSessionRepository.save(session);
    }

    private void startPoll(final Message message,
                           final String userId,
                           final PollSession userPoll) {
        if (responseIsValid(message.getContentRaw())) {
            displayPollInTextChannel(message, userPoll);
            final Timer timer = new Timer();
            timer.schedule(new PollTimer(userId, message.getTextChannel()), parseLong(message.getContentRaw()));
            responseSessionRepository.deleteById(userId);
        }
    }

    private void deletePoll(final Message message,
                            final String requesterId) {
        pollSessionRepository.deleteById(requesterId);
        message.getTextChannel().sendMessage("Your poll has been deleted!").queue();
    }

    private class PollTimer extends TimerTask {


        private final String pollId;
        private final TextChannel textChannel;

        PollTimer(final String pollId,
                  final TextChannel textChannel) {
            super();
            this.pollId = pollId;
            this.textChannel = textChannel;
        }

        @Override
        public void run() {
            pollSessionRepository.findById(pollId).ifPresent(poll -> {
                final String messageId = poll.getMessageId();
                final Message message = textChannel.getMessageById(messageId).complete();
                final Map<String, Integer> resultMap = new HashMap<>();
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("Poll finished! Here are the results...");

                message.getReactions().forEach(messageReaction -> {
                    builder.addField(messageReaction.getReactionEmote().getName(), String.valueOf(messageReaction.getCount()), false);
                });
                textChannel.sendMessage(builder.build()).queue();
                textChannel.deleteMessageById(messageId).reason("Poll has finished").queue();
                pollSessionRepository.deleteById(pollId);
            });
        }
    }

}
