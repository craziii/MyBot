package com.evilduck.configuration.message.handling;

import com.evilduck.entity.CommandDetail;
import com.evilduck.exception.MatchedTooManyCommandsException;
import com.evilduck.repository.SessionRepository;
import com.evilduck.util.CommandHelper;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Router;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageRouter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageRouter.class);

    private final CommandHelper commandHelper;
    private final SessionRepository sessionRepository;

    @Autowired
    public MessageRouter(final CommandHelper commandHelper,
                         final SessionRepository sessionRepository) {
        this.commandHelper = commandHelper;
        this.sessionRepository = sessionRepository;
    }

    @Router
    public String callableOrAutoFireCommandRouter(final org.springframework.messaging.Message<Message> message) {
        final String rawCommand = message.getPayload().getContentRaw();
        final List<CommandDetail> matchedCommands = commandHelper.matchCommandString(getCommandString(rawCommand));
        if (matchedCommands.size() == 1)
            return matchedCommands.get(0).getFullCommand() + "Channel";
        else if (matchedCommands.size() > 1)
            return "errorChannel";
        else if (hasSession(message))
            return "sessionHandlerChannel";
        else {
            LOGGER.info("No explicit Commands matched, checking AutoFire Commands.");
            return "autoFireCommandChannel";
        }
    }

    private boolean hasSession(final org.springframework.messaging.Message<Message> message) {
        return sessionRepository.findById(message.getPayload().getAuthor().getId()).isPresent();
    }

    private static String informOfMatchedCommands(final org.springframework.messaging.Message<Message> message,
                                                  final List<CommandDetail> matchedCommands) {
        final EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Your message matched these Commands");
        for (int i = 0; i < matchedCommands.size(); i++)
            embedBuilder.addField(String.valueOf(i), matchedCommands.get(i).getFullCommand(), true);
        embedBuilder.appendDescription("You can add more letters to your message to or type the whole command name to be more specific");

        message.getPayload().getTextChannel().sendMessage(embedBuilder.build()).queue();
        // TODO: NEED CONTROLLER ADVICE OR ERROR CHANNEL IMPLEMENTATION TO DEAL WITH THESE, CONSOLE OUTPUT MAKES ME SICK
        throw new MatchedTooManyCommandsException("Message matched more than one command, message: \'" +
                message +
                "\', matched commands: " + matchedCommands.toString());
    }

    private String getCommandString(final String rawCommand) {
        return rawCommand.replace("!", "").split(" ")[0];
    }

}
