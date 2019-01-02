package com.evilduck.evilduck.Command.Commands;

import com.evilduck.evilduck.Command.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import org.springframework.stereotype.Component;

import static java.lang.String.format;
import static java.lang.String.valueOf;

@Component
public class Penis implements Command {

    @Override
    public void execute(final org.springframework.messaging.Message<Message> message) {
        final User author = message.getPayload().getAuthor();
        message.getPayload()
                .getTextChannel()
                .sendMessage(new EmbedBuilder()
                        .setTitle(format("%s's penis length", author.getName()))
                        .addField(":eggplant: Length (inches):", valueOf(author.getId().chars().average().orElse(1)), true)
                        .build());

    }

}
