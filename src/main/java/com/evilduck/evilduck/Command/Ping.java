package com.evilduck.evilduck.Command;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class Ping implements GenericCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(Ping.class);

    @Override
    @ServiceActivator(inputChannel = "pingChannel")
    public void execute(final Message<net.dv8tion.jda.core.entities.Message> message) {
        LOGGER.info("Recieved: {}", message.getPayload().getContentRaw());
        final TextChannel originTextChannel = message.getPayload().getTextChannel();
        final long pingValue = message.getPayload().getJDA().getPing();

        originTextChannel.sendMessage(new EmbedBuilder()
                .setTitle("Ping")
                .addField("Value (ms)", String.valueOf(pingValue), true)
                .build())
                .queue();

    }
}
