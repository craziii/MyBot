package com.evilduck.evilduck.Command.Commands;

import com.evilduck.evilduck.Command.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
public class Ping implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(Ping.class);

    @ServiceActivator(inputChannel = "pingChannel")
    public void execute(final org.springframework.messaging.Message<Message> inputMessage) {
        LOGGER.info("Executing Ping Command");
        final Message commandMessage = inputMessage.getPayload();
        commandMessage.getTextChannel()
                .sendMessage(new EmbedBuilder()
                        .setTitle("Ping")
                        .addBlankField(false)
                        .addField(new MessageEmbed.Field(":ping_pong: Heartbeat", String.valueOf(commandMessage.getJDA().getPing()), true))
                        .build())
                .queue();

    }
}
