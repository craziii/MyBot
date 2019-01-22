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

    @Component
public class Ping implements GenericCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(Ping.class);

    @Override
    @ServiceActivator(inputChannel = "pingChannel")
    public void execute(final Message<net.dv8tion.jda.core.entities.Message> message) {
        LOGGER.info("Received: {}", message.getPayload().getContentRaw());
        final TextChannel originTextChannel = message.getPayload().getTextChannel();
        final long pingValue = message.getPayload().getJDA().getPing();

        String pingText = "";
        byte pingColorR = 0;
        byte pingColorG = 0;
        byte pingColorB = 0;
        String pingIcon = "";

        if(pingValue < 100){
            pingText = "This is definitely a good thing";
            pingColorR = 34;
            pingColorG = 139;
            pingColorB = 34;
            pingIcon = "https://discordapp.com/assets/c6b26ba81f44b0c43697852e1e1d1420.svg";
        }
        else if(pingValue < 250){
            pingText = "This is possibly a small hiccup, but if it persists I'm getting my umbrella";
            pingColorR = 255;
            pingColorG = 252;
            pingColorB = 127;
            pingIcon = "https://i.imgur.com/OsfBMXz.png";
        }
        else if(pingValue < 500){
            pingText = "This is possibly a small hiccup, but if it persists its getting to be a pretty bad thing";
            pingColorR = 255;
            pingColorG = 126;
            pingColorB = 71;
            pingIcon = "https://i.imgur.com/22oFoi2.png";
        }
        else{
            pingText = "This is a bad sign, I gotta be honest";
            pingColorR = 255;
            pingColorG = 0;
            pingColorB = 0;
            pingIcon = "https://discordapp.com/assets/15ccaf984f2fafcf3ed5d896763ed510.svg";
        }

            originTextChannel.sendMessage(new EmbedBuilder()
                    .setAuthor("Ping", pingIcon)
                    .setColor(pingColorR, pingColorG, pingColorB)
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
