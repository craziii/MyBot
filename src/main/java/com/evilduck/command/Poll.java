package com.evilduck.command;

import com.evilduck.command.interfaces.IsACommand;
import com.evilduck.command.interfaces.PublicCommand;
import com.evilduck.entity.PollOptions;
import com.evilduck.entity.SessionEntity;
import com.evilduck.repository.SessionRepository;
import com.evilduck.util.CommandHelper;
import net.dv8tion.jda.core.entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@IsACommand(description = "Used to create a poll to ask people stuff", tutorial = "Use !poll with options to choose from", aliases = {"vote"})
public class Poll implements PublicCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(Poll.class);

    private static final String CANCEL_REGEX = "(REMOVE|DELETE|STOP|CANCEL)";
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwyz";
    private final SessionRepository sessionRepository;
    private final CommandHelper commandHelper;

    @Autowired
    public Poll(final SessionRepository sessionRepository,
                final CommandHelper commandHelper) {
        this.sessionRepository = sessionRepository;
        this.commandHelper = commandHelper;
    }

    @Override
    @ServiceActivator(inputChannel = "pollChannel")
    public void execute(final Message message) {
        final String userId = message.getAuthor().getId();
        final Optional<SessionEntity> userPoll = sessionRepository.findById(userId);
        final List<String> args = commandHelper.getArgs(message.getContentRaw());
        final String firstArg = args.size() > 0 ? args.get(0) : "";

        if (isCancelCommand(firstArg)) {
            deletePoll(message, userId);
            LOGGER.info("User {} has deleted their poll", userId);
        } else if (userPoll.isPresent())
            message.getTextChannel().sendMessage("You already have a poll, please enter the number of minutes it should last").queue();
        else if (args.size() > 1) createPoll(message);
        else message.getTextChannel().sendMessage("You must set two or more items for a poll").queue();
    }

    private static boolean isCancelCommand(final String command) {
        return command.toUpperCase().matches(CANCEL_REGEX);
    }

    private SessionEntity createNewPoll(final Message message) {
        final String userId = message.getAuthor().getId();
        LOGGER.info("Creating new poll for user {}", userId);
        final List<String> args;
        final String question;
        if (hasQuestion(message.getContentRaw())) {
            final String[] questionOptions = commandHelper.getArgsAsString(message.getContentRaw(), 0)
                    .split("[?]");
            question = questionOptions[0];
            args = commandHelper.getArgs(questionOptions[1]);
        } else {
            args = commandHelper.getArgs(message.getContentRaw());
            question = null;
        }
        return new SessionEntity(userId, "start", message.getId(), "poll", new PollOptions(5, question + "?", args));
    }

    private boolean hasQuestion(final String rawContent) {
        return rawContent.contains("?");
    }

    private void createPoll(final Message message) {
        final SessionEntity session = createNewPoll(message);
        message.getTextChannel().sendMessage(
                "How many minutes should this poll last?")
                .queue();
        sessionRepository.save(session);
    }

    private void deletePoll(final Message message,
                            final String requesterId) {
        final Optional<SessionEntity> poll = sessionRepository.findById(requesterId);
        sessionRepository.deleteById(requesterId);
        message.getTextChannel().sendMessage(poll.isPresent() ?
                "Your poll has been deleted!" : "You don't have a poll!")
                .queue();
    }

}
