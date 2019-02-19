package com.evilduck.Command;

import com.evilduck.Command.Interface.IsACommand;
import com.evilduck.Command.Interface.ManualCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import org.slf4j.Logger;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.awt.Color;

import static java.lang.String.valueOf;
import static org.slf4j.LoggerFactory.getLogger;

@Component
@IsACommand
public class Ping implements ManualCommand {

    private static final Logger LOGGER = getLogger(Ping.class);

    @Override
    @ServiceActivator(inputChannel = "pingChannel")
    public void execute(final Message message) {
        LOGGER.info("Received: {}", message.getContentRaw());
        final TextChannel originTextChannel = message.getTextChannel();
        final long pingValue = message.getJDA().getPing();
        final PingIndicator pingIndicator = getIndicatorForPing(pingValue);

        final MessageBuilder messageBuilder = new MessageBuilder();
        final EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor("Ping")
                .setThumbnail(pingIndicator.getPingIcon())
                .setColor(pingIndicator.getPingColor())
                .addField("Value (ms)", valueOf(pingValue), true)
                .setDescription(pingIndicator.getPingText());

        messageBuilder.setEmbed(embedBuilder.build());
        originTextChannel.sendMessage(messageBuilder.build()).queue();
    }

    @Override
    public boolean hasPermissionToRun(Member requestingMember) {
        return false;
    }

    @Override
    public void onSuccess(final net.dv8tion.jda.core.entities.Message message) {
        LOGGER.info("ping display success in guild: {} | in text channel: {}",
                message.getGuild(),
                message.getTextChannel());
    }

    @Override
    public void onFail(final Throwable throwable) {
        LOGGER.error("Error executing Ping command!", throwable);
    }

    private static PingIndicator getIndicatorForPing(final long pingValue) {
        if (pingValue < 200L) {
            return new PingIndicator(
                    "This is definitely a good thing",
                    new Color(0, 233, 10),
                    "https://i.imgur.com/7SzeRXQ.png");
        } else if (pingValue < 300L) {
            return new PingIndicator(
                    "This is possibly a small hiccup, but if it persists I'm getting my umbrella",
                    new Color(221, 255, 0),
                    "https://i.imgur.com/OsfBMXz.png");
        } else if (pingValue < 400L) {
            return new PingIndicator(
                    "This is possibly a small hiccup, but if it persists its getting to be a pretty bad thing",
                    new Color(255, 120, 0),
                    "https://i.imgur.com/22oFoi2.png");
        } else {
            return new PingIndicator(
                    "This is a bad sign, I gotta be honest",
                    new Color(255, 0, 0),
                    "https://i.imgur.com/AmWlvB8.png");
        }
    }

    private static class PingIndicator {

        private final String pingText;
        private final Color pingColor;
        private final String pingIcon;
        private final String fileName;

        private PingIndicator(final String pingText,
                              final Color pingColor,
                              final String pingIcon) {
            this.pingText = pingText;
            this.pingColor = pingColor;
            this.pingIcon = pingIcon;
            final String[] urlParts = pingIcon.split("/");
            this.fileName = urlParts[urlParts.length - 1];
        }

        String getPingText() {
            return pingText;
        }

        Color getPingColor() {
            return pingColor;
        }

        String getPingIcon() {
            return pingIcon;
        }

        public String getFileName() {
            return fileName;
        }
    }

}
