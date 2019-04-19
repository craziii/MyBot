package com.evilduck.command;

import com.evilduck.command.standards.IsACommand;
import com.evilduck.command.standards.PublicCommand;
import com.evilduck.repository.PollSessionRepository;
import com.evilduck.session.PollSession;
import com.evilduck.util.CommandHelper;
import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    private final PollSessionRepository pollSessionRepository;
    private final CommandHelper commandHelper;

    public Poll(final PollSessionRepository pollSessionRepository,
                final CommandHelper commandHelper) {
        this.pollSessionRepository = pollSessionRepository;
        this.commandHelper = commandHelper;
    }

    @Override
    @ServiceActivator(inputChannel = "pollChannel")
    public void execute(final Message message) {
        final String requesterId = message.getAuthor().getDiscriminator();
        final Optional<PollSession> userPoll = pollSessionRepository.findById(requesterId);
        final List<String> args = commandHelper.getArgs(message.getContentRaw());

        if (args.size() > 0 && args.get(0).toUpperCase().matches("(REMOVE|DELETE|STOP|CANCEL)")) {
            pollSessionRepository.deleteById(requesterId);
        } else if (userPoll.isPresent()) {
            if (args.isEmpty()) {
                message.getTextChannel()
                        .sendMessage("You already have a poll, please send `poll cancel` to cancel your poll before starting a new one")
                        .queue();
            }

        } else {
            final PollSession newPoll = createNewPoll(requesterId);
            displayPollInTextChannel(message, newPoll);
            pollSessionRepository.save(newPoll);
        }
    }

    private static void displayPollInTextChannel(final Message message,
                                                 final PollSession newPoll) {
        final EmbedBuilder embedBuilder = new EmbedBuilder().setTitle(message.getAuthor().getName() + "'s Poll");
        final Map<String, Integer> options = newPoll.getOptions();
        final List<String> optionsKeys = new ArrayList<>(options.keySet());
        for (int i = 0; i < options.size(); i++) embedBuilder.addField(String.valueOf(i), optionsKeys.get(i), true);
        embedBuilder.setDescription("Vote via the reaction icons below");

        message.getTextChannel().sendMessage(embedBuilder.build()).queue(success -> {
            final int numOfOptions = newPoll.getOptions().size();
            for (int i = 0; i < numOfOptions; i++) success.addReaction(EmojiParser.parseToUnicode((String) NUMBER_EMOJIS.get(i))).queue();
            newPoll.setMessageId(success.getId());
        });
    }

    private static PollSession createNewPoll(final String requesterId) {
        final Map<String, Integer> pollOptions = new LinkedHashMap<>(); // TODO: TEST ORDER INERTION SAFETY
        return new PollSession(requesterId, pollOptions);
    }
    
}
