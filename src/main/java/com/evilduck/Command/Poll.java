package com.evilduck.Command;

import com.evilduck.Command.Interface.IsACommand;
import com.evilduck.Command.Interface.PublicCommand;
import com.evilduck.Repository.PollSessionRepository;
import com.evilduck.Session.PollSession;
import com.evilduck.Util.CommandHelper;
import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@IsACommand
public class Poll implements PublicCommand {

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

        if (userPoll.isPresent()) {
            if (args.get(1).matches("i?:(REMOVE|DELETE|STOP)")) {
                pollSessionRepository.deleteById(requesterId);
            }
            // TODO: ADD CANCEL OPTION
            message.getTextChannel().sendMessage("You already have a poll, please send `poll cancel` to cancel your poll before starting a new one").queue();
        } else {
            final PollSession newPoll = createNewPoll(requesterId, args, pollSessionRepository);
            displayPollInTextChannel(message, newPoll);
        }

    }

    private static void displayPollInTextChannel(final Message message,
                                                 final PollSession newPoll) {
        final EmbedBuilder embedBuilder = new EmbedBuilder().setTitle(message.getAuthor().getName() + "'s Poll");
        final Map<String, Integer> options = newPoll.getOptions();
        options.forEach((key, value) -> embedBuilder.addField(key, value.toString(), true));
        embedBuilder.setDescription("Vote via the reaction icons below");
        message.getTextChannel().sendMessage(embedBuilder.build()).queue(success -> {
            success.addReaction(EmojiParser.parseToUnicode(":white_check_mark:")).queue();
            success.addReaction(EmojiParser.parseToUnicode(":x:")).queue();
        });
    }

    private static PollSession createNewPoll(final String requesterId,
                                             final List<String> args,
                                             final PollSessionRepository pollSessionRepository) {
        final List<String> pollOptionList = args.subList(1, args.size());
        final Map<String, Integer> pollOptions = new ConcurrentHashMap<>();
        pollOptionList.forEach(arg -> pollOptions.put(arg, 0));
        final PollSession pollSession = new PollSession(requesterId, pollOptions);
        return pollSessionRepository.save(pollSession);
    }
}
