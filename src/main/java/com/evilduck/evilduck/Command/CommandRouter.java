package com.evilduck.evilduck.Command;

import net.dv8tion.jda.core.entities.Message;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.MessageChannel;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static org.springframework.integration.dsl.IntegrationFlows.from;
import static org.springframework.integration.handler.LoggingHandler.Level.INFO;

@MessageEndpoint
public class CommandRouter {

    @Bean
    public IntegrationFlow commandFlow(final MessageChannel commandInputChannel,
                                       final CommandArgsLengthParser commandArgsLengthParser,
                                       final MessageChannel pingChannel,
                                       final MessageChannel penisChannel) {
        return from(commandInputChannel)
                .transform(commandArgsLengthParser)
                .log(INFO, "Routing message to correct command", m -> m)
                .<org.springframework.messaging.Message<Message>, String>route(p ->
                                requireNonNull(p.getHeaders().get("args", String[].class))[0],
                        m -> m.channelMapping("ping", pingChannel.toString())
                                .channelMapping("penis", penisChannel.toString()))
                .get();


    }

    // TODO: GET HEADERVALUEROUTER WORKING!
    @Bean
    private IntegrationFlow commandHeaderRouter() {
        return IntegrationFlows.from("").handle(m -> {ofNullable(m.getHeaders().get("args", String[].class))
                .orElse(new String[] {"errorChannel"})[0]
                .toLowerCase()
                .concat("Channel"))});
    }

}
