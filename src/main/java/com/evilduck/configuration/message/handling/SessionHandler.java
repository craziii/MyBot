package com.evilduck.configuration.message.handling;

import com.evilduck.callback.PollFinisher;
import com.evilduck.entity.PollOptions;
import com.evilduck.entity.SessionEntity;
import com.evilduck.enums.AlphabetEmojis;
import com.evilduck.repository.SessionRepository;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Timer;

import static com.evilduck.enums.AlphabetEmojis.unicodeForLetter;

@Component
public class SessionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionHandler.class);
    private static final String CANCEL_REGEX = "(REMOVE|DELETE|STOP|CANCEL)";
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwyz";

    private final SessionRepository sessionRepository;

    @Autowired
    public SessionHandler(final SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @ServiceActivator(inputChannel = "sessionHandlerChannel")
    public void continueSession(final Message message) {
        final Optional<SessionEntity> sessionOptional = sessionRepository.findById(message.getAuthor().getId());
        if (!sessionOptional.isPresent()) return;
        final SessionEntity session = sessionOptional.get();
        switch (session.getSessionType()) {
            case "poll": pollSession(message, session);

        }

    }

    private void pollSession(final Message message,
                             final SessionEntity session) {
        final String raw = message.getContentRaw();
        if (!StringUtils.isNumeric(raw)) message.getTextChannel().sendMessage("You must specify a number of minutes").queue();
        final PollOptions pollOptions = (PollOptions) session.getSessionDetail();
        if (session.getNextStep().equals("start")) {
            pollOptions.setTimeToLive(Long.parseLong(raw));
            displayPollInTextChannel(message, pollOptions);

        }


    }

    private void displayPollInTextChannel(final Message message,
                                          final PollOptions options) {
        final String userId = message.getAuthor().getId();
        LOGGER.info("Displaying poll belonging to user {}", userId);
        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(options.getQuestion() == null ? message.getAuthor().getName() + "'s Poll" : options.getQuestion());
        final List<String> pollOptions = options.getOptions();
        for (int i = 0; i < pollOptions.size(); i++)
            embedBuilder.addField(AlphabetEmojis.unicodeForLetter(ALPHABET.charAt(i)), pollOptions.get(i), true);
        embedBuilder.setDescription("Vote via the reaction icons below");

        message.getTextChannel().sendMessage(embedBuilder.build()).queue(
                success -> pollShowSuccess(options, success, userId),
                failure -> pollShowFailure(message, userId)
        );
    }

    private void pollShowSuccess(final PollOptions pollOptions,
                                 final Message success,
                                 final String userId) {
        sessionRepository.deleteById(userId);
        for (int i = 0; i < pollOptions.getOptions().size(); i++)
            success.addReaction(unicodeForLetter(ALPHABET.charAt(i))).queue();
        LOGGER.info("Successfully started poll!");
        final Timer timer = new Timer();
        timer.schedule(new PollFinisher(success.getTextChannel(), success.getId(), pollOptions),
                DateTime.now().plus(Duration.standardMinutes(pollOptions.getTimeToLive())).toDate());
    }

    private void pollShowFailure(final Message message,
                                 final String userId) {
        message.getTextChannel().sendMessage("Your poll be fucked my dude ¯\\_(ツ)_/¯\n" +
                "I'll go ahead and delete it for you").queue();
        sessionRepository.deleteById(userId);
        LOGGER.warn("There was a problem starting this poll! All remnants deleted");
    }

}
