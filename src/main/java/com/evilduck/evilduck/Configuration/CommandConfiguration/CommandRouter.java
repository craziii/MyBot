package com.evilduck.evilduck.Configuration.CommandConfiguration;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
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
    private final JDA jda;

    @Autowired
    public CommandRouter(final CommandFormatter commandFormatter,
                         final MessageChannel commandInputChannel,
                         final MessageChannel pingChannel,
                         final MessageChannel penisChannel,
                         final JDA jda) {
        this.commandFormatter = commandFormatter;
        this.commandInputChannel = commandInputChannel;
        this.pingChannel = pingChannel;
        this.penisChannel = penisChannel;
        this.jda = jda;
    }

    @Bean
    public IntegrationFlow commandFlow() {
        return IntegrationFlows.from(commandInputChannel)
                .transform(commandFormatter)
                .log("info")
                .filter((Message p) -> !p.getAuthor().getDiscriminator().equals(jda.getSelfUser().getDiscriminator()))
                .<net.dv8tion.jda.core.entities.Message, String>route(p -> p.getContentRaw().replace("!", ""),
                        m -> m.channelMapping("ping", "pingChannel")
                .channelMapping("penis", "penisChannel"))
                .get();
    }

}
