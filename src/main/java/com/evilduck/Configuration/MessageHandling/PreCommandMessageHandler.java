package com.evilduck.Configuration.MessageHandling;

import net.dv8tion.jda.core.entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.MessageChannel;

@MessageEndpoint
public class PreCommandMessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PreCommandMessageHandler.class);

    private final MessageFilter messageFilter;
    private final MessageRouter messageRouter;
    private final CommandFormatter commandFormatter;
    private final MessageChannel commandInputChannel;

    @Autowired
    public PreCommandMessageHandler(final MessageFilter messageFilter,
                                    final MessageRouter messageRouter,
                                    final CommandFormatter commandFormatter,
                                    final MessageChannel commandInputChannel) {
        this.messageFilter = messageFilter;
        this.messageRouter = messageRouter;
        this.commandFormatter = commandFormatter;
        this.commandInputChannel = commandInputChannel;
    }


    @Bean
    public IntegrationFlow generalCommandFlow() {
        return IntegrationFlows.from(commandInputChannel)
                .transform(commandFormatter)
                .filter(messageFilter)
                .wireTap("incomingMessageLoggingChannel")
                .route(messageRouter)
                .get();
    }

    @ServiceActivator(inputChannel = "incomingMessageLoggingChannel")
    public void incomingMessageLogger(final Message message) {
        LOGGER.info(" ========== ========== ========== ========== ==========");
        LOGGER.info("Message Received, ID: {} ", message.getId());
        LOGGER.info("Text Channel: {}", message.getTextChannel());
        LOGGER.info("Author: {}", message.getAuthor());
        LOGGER.info("Raw Content: {}", message.getContentRaw());
        LOGGER.info(" ========== ========== ========== ========== ==========");
    }


}
