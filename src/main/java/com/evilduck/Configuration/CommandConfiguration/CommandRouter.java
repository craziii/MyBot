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
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.MessageChannel;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

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
    public IntegrationFlow generalCommandFlow() {
        return IntegrationFlows.from(commandInputChannel)   //TODO: MAYBE USE RAW FLOWS WITHOUT CHANNELS???
                .transform(commandFormatter)
                .filter((Message p) -> isNotThisBot(p, jda))
                .<Message, Boolean>route(
                        message -> commandDetailRepository.findOneByFullCommand(message.getContentRaw()) != null,
                        m -> m.subFlowMapping(TRUE, callableCommandFlow())
                                .subFlowMapping(FALSE, autoFireCommandFlow()))
                .get();
    }

    @Bean
    public IntegrationFlow callableCommandFlow() {
        return flow -> flow.channel("resolveCallableCommandChannel");
    }

    @Bean
    public IntegrationFlow autoFireCommandFlow() {
        return flow -> flow.channel("resolveAutoFireCommandChannel");
    }


    @Router(inputChannel = "resolveCallableCommandChannel")
    public String commandChannelRouter(final Object payload) {
        if (payload instanceof Message)
            return getCommandString(((Message) payload).getContentRaw()) + "Channel";
        else
            throw new TypeMismatchException(payload, Message.class);
    }

    @Router(inputChannel = "resolveAutoFireCommandChannel")
    public String autoFireCommandChannelRouter(final Object payload) {
        if (payload instanceof Message)
            return getCommandString(((Message) payload).getContentRaw()) + "Channel";
        else
            throw new TypeMismatchException(payload, Message.class);
    }

    @Bean
    public MessageChannel callableCommandChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel autoFireCommandChannel() {
        return new DirectChannel();
    }

    private String getCommandString(final String rawCommand) {
        return rawCommand.replace("!", "").split(" ")[0];
    }


    private static boolean isNotThisBot(final Message payload,
                                        final JDA jda) {
        return !payload.getAuthor().getDiscriminator()
                .equals(jda.getSelfUser().getDiscriminator());
    }
}
