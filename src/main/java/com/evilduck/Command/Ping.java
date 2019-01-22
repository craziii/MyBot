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
        final PingIndicator pingIndicator = getIndicatorForPing(pingValue);

        originTextChannel.sendMessage(new EmbedBuilder()
                .setAuthor("Ping", pingIndicator.getPingIcon())
                .setColor(pingIndicator.getPingColor())
                .addField("Value (ms)", valueOf(pingValue), true)
                .addField("", pingIndicator.getPingText(), false)
                .build())
                .queue();
    }

    private static PingIndicator getIndicatorForPing(final long pingValue) {
        if (pingValue < 100L) {
            return new PingIndicator(
                    "This is definitely a good thing",
                    new Color(34, 139, 34),
                    "https://discordapp.com/assets/c6b26ba81f44b0c43697852e1e1d1420.svg");
        } else if (pingValue < 250L) {
            return new PingIndicator(
                    "This is possibly a small hiccup, but if it persists I'm getting my umbrella",
                    new Color(255, 252, 127),
                    "https://i.imgur.com/OsfBMXz.png");
        } else if (pingValue < 500L) {
            return new PingIndicator(
                    "This is possibly a small hiccup, but if it persists its getting to be a pretty bad thing",
                    new Color(255, 126, 71),
                    "https://i.imgur.com/22oFoi2.png");
        } else {
            return new PingIndicator(
                    "This is a bad sign, I gotta be honest",
                    new Color(255, 0, 0),
                    "https://discordapp.com/assets/15ccaf984f2fafcf3ed5d896763ed510.svg");
        }
    }

    @Override
    public void onSuccess() {
        LOGGER.info("ping display success in guild: {} | in text channel: {}", message.getPayload().getGuild(), message.getPayload().getTextChannel());
    }

    @Override
    public void onFail() {
        LOGGER.info("ping display failure in guild: {} | in text channel: {}", message.getPayload().getGuild(), message.getPayload().getTextChannel());
    }

    private static class PingIndicator {
        private final String pingText;
        private final Color pingColor;
        private final String pingIcon;

        private PingIndicator(final String pingText,
                              final Color pingColor,
                              final String pingIcon) {
            this.pingText = pingText;
            this.pingColor = pingColor;
            this.pingIcon = pingIcon;
        }

        public String getPingText() {
            return pingText;
        }

        public Color getPingColor() {
            return pingColor;
        }

        public String getPingIcon() {
            return pingIcon;
        }
    }
}
