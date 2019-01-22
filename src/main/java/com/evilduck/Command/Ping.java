package com.evilduck.Command;

import com.evilduck.Configuration.CommandConfiguration.GenericCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.awt.*;

import static java.lang.String.valueOf;

@Component
public class Ping implements GenericCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(Ping.class);

    @Override
    @ServiceActivator(inputChannel = "pingChannel")
    public void execute(final Message<net.dv8tion.jda.core.entities.Message> message) {
        LOGGER.info("Received: {}", message.getPayload().getContentRaw());
        final TextChannel originTextChannel = message.getPayload().getTextChannel();
        final long pingValue = message.getPayload().getJDA().getPing();

        if (pingValue < 100) {
            originTextChannel.sendMessage(new EmbedBuilder()
                    .setAuthor("Ping", "https://discordapp.com/assets/c6b26ba81f44b0c43697852e1e1d1420.svg")
                    .setColor(new Color(0, 255, 0))
                    .addField("Value (ms)", valueOf(pingValue), true)
                    .addField("", "This is definitely a good thing", false)
                    .build())
                    .queue();
        } else if (pingValue > 100 && pingValue <= 250) {
            originTextChannel.sendMessage(new EmbedBuilder()
                    .setAuthor("Ping", "https://i.imgur.com/OsfBMXz.png")
                    .setColor(new Color(255, 255, 0))
                    .addField("Value (ms)", valueOf(pingValue), true)
                    .addField("", "This is possibly a small hiccup, but if it persists I'm getting my umbrella", false)
                    .build())
                    .queue();
        } else if (pingValue > 250 && pingValue <= 500) {
            originTextChannel.sendMessage(new EmbedBuilder()
                    .setAuthor("Ping", "https://i.imgur.com/22oFoi2.png")
                    .setColor(new Color(255, 165, 0))
                    .addField("Value (ms)", valueOf(pingValue), true)
                    .addField("", "This is possibly a small hiccup, but if it persists its getting to be a pretty bad thing", false)
                    .build())
                    .queue();
        } else {
            originTextChannel.sendMessage(new EmbedBuilder()
                    .setAuthor("Ping", "https://discordapp.com/assets/15ccaf984f2fafcf3ed5d896763ed510.svg")
                    .setColor(new Color(255, 0, 0))
                    .addField("Value (ms)", valueOf(pingValue), true)
                    .addField("", "This is a bad sign, I gotta be honest", false)
                    .build())
                    .queue();
        }
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFail() {

    }
}
