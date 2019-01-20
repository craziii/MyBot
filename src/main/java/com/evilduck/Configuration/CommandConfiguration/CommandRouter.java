package com.evilduck.Configuration.CommandConfiguration;

import com.evilduck.Repository.CommandDetailRepository;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.MessageChannel;

@MessageEndpoint
public class CommandRouter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandRouter.class);

    private final CommandFormatter commandFormatter;
    private final MessageChannel commandInputChannel;
    private final CommandDetailRepository commandDetailRepository;
    private final JDA jda;

    @Autowired
    public CommandRouter(final CommandFormatter commandFormatter,
                         final MessageChannel commandInputChannel,
                         CommandDetailRepository commandDetailRepository, final JDA jda) {
        this.commandFormatter = commandFormatter;
        this.commandInputChannel = commandInputChannel;
        this.commandDetailRepository = commandDetailRepository;
        this.jda = jda;
    }

    @Bean
    public IntegrationFlow commandFlow() {
        return IntegrationFlows.from(commandInputChannel)
                .transform(commandFormatter)
                .filter((Message p) -> !p.getAuthor().getDiscriminator()
                        .equals(jda.getSelfUser().getDiscriminator()))
                .filter((Message p) -> commandDetailRepository
                        .findOneByFullCommand(getCommandString(p.getContentRaw())) != null)
                .channel("resolveCommandChannel")
                .get();
    }

    @Router(inputChannel = "resolveCommandChannel")
    public String commandChannelRouter(final Object payload) {
        if (payload instanceof Message) {
            return getCommandString(((Message) payload).getContentRaw()) + "Channel";
        } else
            throw new TypeMismatchException(payload, Message.class);
    }

    private String getCommandString(final String rawCommand) {
        return rawCommand.replace("!", "").split(" ")[0];
    }

}
