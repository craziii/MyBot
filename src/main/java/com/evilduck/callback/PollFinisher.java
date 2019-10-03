package com.evilduck.callback;

import com.evilduck.entity.PollOptions;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.List;
import java.util.TimerTask;

public class PollFinisher extends TimerTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(PollFinisher.class);
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwyz";

    private final TextChannel textChannel;
    private final String messageId;
    private final PollOptions pollOptions;

    public PollFinisher(final TextChannel textChannel,
                        final String messageId,
                        final PollOptions pollOptions) {
        this.textChannel = textChannel;
        this.messageId = messageId;
        this.pollOptions = pollOptions;
    }


    @Override
    public void run() {
        final EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Poll finished! Here are the results...");
        final Message pollMessage = textChannel.getMessageById(messageId).complete();
        final List<MessageReaction> reactions = pollMessage.getReactions();
        for (int i = 0; i < reactions.size(); i++) {
            builder.addField(
                    pollOptions.getOptions().get(i),
                    (reactions.get(i).getCount() - 1) + " Votes",
                    true);
        }
        builder.setColor(Color.CYAN);
        textChannel.sendMessage(builder.build()).queue();
        textChannel.deleteMessageById(messageId).reason("Poll has finished").queue();
        LOGGER.info("User {}'s Poll has finished", pollMessage.getAuthor().getDiscriminator());
    }

}
