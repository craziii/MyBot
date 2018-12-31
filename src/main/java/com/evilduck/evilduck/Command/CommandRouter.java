package com.evilduck.evilduck.Command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import java.util.Objects;

import static com.evilduck.evilduck.Command.CommandType.PING;

@MessageEndpoint
public class CommandRouter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandRouter.class);

    private final CommandFormatter commandFormatter;
    private final MessageChannel commandInputChannel;
    private final MessageChannel pingChannel;

    @Autowired
    public CommandRouter(final CommandFormatter commandFormatter,
                         final MessageChannel commandInputChannel,
                         final MessageChannel pingChannel) {
        this.commandFormatter = commandFormatter;
        this.commandInputChannel = commandInputChannel;
        this.pingChannel = pingChannel;
    }

    @Bean
    public IntegrationFlow commandFlow() {
        return IntegrationFlows.from(commandInputChannel)
                .transform(commandFormatter)
                .log("info")
                .<net.dv8tion.jda.core.entities.Message, String>route(p -> p.getContentRaw().replace("!", ""),
                        m -> m.channelMapping("ping", "pingChannel"))
                .get();


    }

}
