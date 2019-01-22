package com.evilduck.Command;

import com.evilduck.Configuration.CommandConfiguration.GenericCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.awt.Color;

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

        final String pingText;
        final Color pingColor;
        final String pingIcon;

        if (pingValue < 100) {
            pingText = "This is definitely a good thing";
            pingColor = new Color(34, 139, 34);
            pingIcon = "https://discordapp.com/assets/c6b26ba81f44b0c43697852e1e1d1420.svg";
        } else if (pingValue < 250) {
            pingText = "This is possibly a small hiccup, but if it persists I'm getting my umbrella";
            pingColor = new Color(255, 252, 127);
            pingIcon = "https://i.imgur.com/OsfBMXz.png";
        } else if (pingValue < 500) {
            pingText = "This is possibly a small hiccup, but if it persists its getting to be a pretty bad thing";
            pingColor = new Color(255, 126, 71);
            pingIcon = "https://i.imgur.com/22oFoi2.png";
        } else {
            pingText = "This is a bad sign, I gotta be honest";
            pingColor = new Color(255, 0, 0);
            pingIcon = "https://discordapp.com/assets/15ccaf984f2fafcf3ed5d896763ed510.svg";
        }

        originTextChannel.sendMessage(new EmbedBuilder()
                .setAuthor("Ping", pingIcon)
                .setColor(pingColor)
                .addField("Value (ms)", valueOf(pingValue), true)
                .addField("", pingText, false)
                .build())
                .queue();
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFail() {

    }
}
