package com.evilduck.command;

import com.evilduck.command.interfaces.IsACommand;
import com.evilduck.command.interfaces.PublicCommand;
import com.evilduck.repository.PollSessionRepository;
import com.evilduck.repository.ResponseSessionRepository;
import com.evilduck.session.PollSession;
import com.evilduck.session.ResponseSession;
import com.evilduck.util.CommandHelper;
import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import static com.evilduck.util.TextColorPrefix.GREEN;
import static com.evilduck.util.TextColorPrefix.ORANGE;
import static com.evilduck.util.TextColorPrefix.RED;
import static com.evilduck.util.TextColorPrefix.YELLOW;
import static com.evilduck.util.TextColorPrefix.getTextInColor;
import static java.lang.Long.parseLong;

@Component
@IsACommand(aliases = {"vote"})
public class Poll implements PublicCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(Poll.class);

    private static final String CANCEL_REGEX = "(REMOVE|DELETE|STOP|CANCEL)";
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

    @Autowired
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
        final Optional<PollSession> userPoll = pollSessionRepository.findById(userId);
        final List<String> args = commandHelper.getArgs(message.getContentRaw());
        final String firstArg = args.size() > 0 ? args.get(0) : "";

        if (responseSessionRepository.findById(userId).isPresent() && userPoll.isPresent())
            startPoll(message, userId, userPoll.get());
        else if (userPoll.isPresent() || isCancelCommand(firstArg)) existingPollActions(message, userId);
        else createPoll(message, userId);
    }

    private static boolean isCancelCommand(final String command) {
        return command.toUpperCase().matches(CANCEL_REGEX);
    }

    private void existingPollActions(final Message message,
                                     final String userId) {
        final String discriminator = message.getAuthor().getDiscriminator();
        LOGGER.info("User {} has existing poll, testing for actions...", discriminator);

        final List<String> args = commandHelper.getArgs(message.getContentRaw());
        if (args.size() > 0 && isCancelCommand(args.get(0))) {
            deletePoll(message, userId);
            LOGGER.info("User {} has deleted their poll", discriminator);
        } else {
            // Reckon I could format this better, but I don't know how....
            message.getTextChannel()
                    .sendMessage("You already have a poll, " +
                            "please send `poll cancel` to cancel " +
                            "your poll before starting a new one").queue();
            LOGGER.info("User {} has attempted an action whilst already owning a poll, ignoring", discriminator);
        }
    }

    private static boolean responseIsValid(final String message) {
        return message.matches("[0-9]*");
    }

    private void displayPollInTextChannel(final Message message,
                                          final PollSession newPoll) {
        final String discriminator = message.getAuthor().getDiscriminator();
        LOGGER.info("Displaying poll belonging to user {}", discriminator);

        final PollSession actualPoll = pollSessionRepository.findById(discriminator)
                .orElse(null);
        final EmbedBuilder embedBuilder = new EmbedBuilder().setTitle(message.getAuthor().getName() + "'s Poll");
        final Map<String, Integer> options = newPoll.getOptions();
        final List<String> optionsKeys = new ArrayList<>(options.keySet());
        for (int i = 0; i < options.size(); i++) embedBuilder.addField(String.valueOf(i), optionsKeys.get(i), true);
        embedBuilder.setDescription("Vote via the reaction icons below");

        message.getTextChannel().sendMessage(embedBuilder.build()).queue(
                success -> pollShowSuccess(actualPoll, success),
                failure -> pollShowFailure(message, discriminator)
        );
    }

    private void pollShowSuccess(PollSession actualPoll, Message success) {
        for (int i = 0; i < actualPoll.getOptions().size(); i++)
            success.addReaction(EmojiParser.parseToUnicode((String) NUMBER_EMOJIS.get(i))).queue();
        actualPoll.setMessageId(success.getId());
        pollSessionRepository.save(actualPoll);
        LOGGER.info("Successfully started poll!");
    }

    private void pollShowFailure(Message message, String discriminator) {
        message.getTextChannel().sendMessage("Your poll be fucked my dude ¯\\_(ツ)_/¯\n" +
                "I'll go ahead and delete it for you").queue();
        responseSessionRepository.deleteById(discriminator);
        pollSessionRepository.deleteById(discriminator);
        LOGGER.warn("There was a problem starting this poll! All remnants deleted");
    }

    private PollSession createNewPoll(final String requesterId,
                                      final Message message) {
        LOGGER.info("Creating new poll for user {}", message.getAuthor().getDiscriminator());
        final List<String> args = commandHelper.getArgs(message.getContentRaw());
        final Map<String, Integer> pollOptions = new LinkedHashMap<>();
        for (int i = 0; i < args.size(); i++) pollOptions.put(args.get(i), i);

        return new PollSession(requesterId, "null", pollOptions);
    }

    private void createPoll(final Message message,
                            final String requesterId) {
        final PollSession newPoll = createNewPoll(requesterId, message);
        pollSessionRepository.save(newPoll);
        message.getTextChannel().sendMessage(
                getTextInColor(YELLOW, "How many minutes should this poll last?"))
                .queue();

        final ResponseSession session = new ResponseSession(requesterId, 0, "poll");
        responseSessionRepository.save(session);
    }

    private void startPoll(final Message message,
                           final String userId,
                           final PollSession userPoll) {
        if (responseIsValid(message.getContentRaw())) {
            displayPollInTextChannel(message, userPoll);
            final Timer timer = new Timer();
            timer.schedule(new PollTimer(userId, message.getTextChannel()), parseLong(message.getContentRaw()) * 60000);
            responseSessionRepository.deleteById(userId);
        } else message.getTextChannel().sendMessage(getTextInColor(ORANGE, "Invalid input, please specify a number!")).queue();
    }

    private void deletePoll(final Message message,
                            final String requesterId) {
        final Optional<PollSession> poll = pollSessionRepository.findById(requesterId);
        pollSessionRepository.deleteById(requesterId);
        message.getTextChannel().sendMessage(poll.isPresent() ?
                getTextInColor(GREEN, "Your poll has been deleted!") :
                getTextInColor(RED, "You don't have a poll!")
        ).queue();
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
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("Poll finished! Here are the results...");

                final List<MessageReaction> reactions = message.getReactions();
                for (int i = 0; i < reactions.size(); i++) {
                    final List<String> keys = new ArrayList<>(poll.getOptions().keySet());
                    builder.addField(
                            keys.get(i),
                            String.valueOf(reactions.get(i).getCount() - 1),
                            true);

                }

                builder.setColor(Color.CYAN);
                textChannel.sendMessage(builder.build()).queue();
                textChannel.deleteMessageById(messageId).reason("Poll has finished").queue();
                pollSessionRepository.deleteById(pollId);
                LOGGER.info("User {}'s Poll has finished, displayed results and deleted poll.",
                        message.getAuthor().getDiscriminator());
            });
        }
    }

}
