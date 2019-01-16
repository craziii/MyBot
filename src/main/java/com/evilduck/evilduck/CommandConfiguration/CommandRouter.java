package com.evilduck.evilduck.CommandConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.MessageChannel;

@MessageEndpoint
public class CommandRouter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandRouter.class);

    private final CommandFormatter commandFormatter;
    private final MessageChannel commandInputChannel;
    private final MessageChannel pingChannel;
    private final MessageChannel penisChannel;

    @Autowired
    public CommandRouter(final CommandFormatter commandFormatter,
                         final MessageChannel commandInputChannel,
                         final MessageChannel pingChannel,
                         final MessageChannel penisChannel) {
        this.commandFormatter = commandFormatter;
        this.commandInputChannel = commandInputChannel;
        this.pingChannel = pingChannel;
        this.penisChannel = penisChannel;
    }

    @Bean
    public IntegrationFlow commandFlow() {
        return IntegrationFlows.from(commandInputChannel)
                .transform(commandFormatter)
                .log("info")
                .<net.dv8tion.jda.core.entities.Message, String>route(p -> p.getContentRaw().replace("!", ""),
                        m -> m.channelMapping("ping", "pingChannel")
                .channelMapping("penis", "penisChannel"))
                .get();
    }

}
