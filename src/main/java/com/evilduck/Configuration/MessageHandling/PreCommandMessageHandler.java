package com.evilduck.Configuration.MessageHandling;

import com.evilduck.Repository.CommandDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.MessageChannel;

@MessageEndpoint
public class PreCommandMessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PreCommandMessageHandler.class);

    private final MessageChannel commandInputChannel;
    private final CommandFormatter commandFormatter;
    private final MessageFilter messageFilter;
    private final MessageRouter messageRouter;
    private final CommandDetailRepository commandDetailRepository;

    @Autowired
    public PreCommandMessageHandler(final CommandFormatter commandFormatter,
                                    final MessageChannel commandInputChannel,
                                    final MessageFilter messageFilter,
                                    final MessageRouter messageRouter,
                                    final CommandDetailRepository commandDetailRepository) {
        this.commandFormatter = commandFormatter;
        this.commandInputChannel = commandInputChannel;
        this.messageFilter = messageFilter;
        this.messageRouter = messageRouter;
        this.commandDetailRepository = commandDetailRepository;
    }

    @Bean
    public IntegrationFlow generalCommandFlow() {
        return IntegrationFlows.from(commandInputChannel)
                .transform(commandFormatter)
                .filter(messageFilter)
                .route(messageRouter)
                .get();
    }

    // TODO: THESE SEEM REDUNDANT, CAN WE CALL THE ROUTERS DIRECTLY WITHOUT CHANNELS/FLOWS ?
    @Bean
    public IntegrationFlow callableCommandFlow() {
        return flow -> flow.channel("resolveCallableCommandChannel");
    }

    @Bean
    public IntegrationFlow autoFireCommandFlow() {
        return flow -> flow.channel("resolveAutoFireCommandChannel");
    }

}
