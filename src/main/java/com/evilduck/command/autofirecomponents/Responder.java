package com.evilduck.command.autofirecomponents;

import com.evilduck.command.interfaces.GenericCommand;
import com.evilduck.command.interfaces.IsACommand;
import com.evilduck.repository.ResponseSessionRepository;
import com.evilduck.session.ResponseSession;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@IsACommand(description = "Automatic command that responds to created sessions", tutorial = "N/A", callable = false)
public class Responder implements GenericCommand {

    private static final Logger logger = LoggerFactory.getLogger(Responder.class);

    private final ResponseSessionRepository responseSessionRepository;
    private final DirectChannel pollChannel;

    @Autowired
    public Responder(final ResponseSessionRepository responseSessionRepository,
                     @Qualifier("pollChannel") final DirectChannel pollChannel) {
        this.responseSessionRepository = responseSessionRepository;
        this.pollChannel = pollChannel;
    }

    @Override
    @ServiceActivator(inputChannel = "autoFireCommandChannel")
    public void execute(final Message message) {
        final User author = message.getAuthor();
        logger.info("Checking for commands awaiting response from user {}...", author.getName());
        final Optional<ResponseSession> session = responseSessionRepository.findById(author.getDiscriminator());
        if (session.isPresent()) {
            // How might I get this to work...?
            final String channelName = session.get().getCommandName() + "Channel";
            pollChannel.send(MessageBuilder.withPayload(message).build());
        } else {
            logger.info("No sessions found for user");
        }
    }

}
