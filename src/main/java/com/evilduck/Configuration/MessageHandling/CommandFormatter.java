package com.evilduck.Configuration.MessageHandling;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.SelfUser;
import net.dv8tion.jda.core.entities.impl.ReceivedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.Long.parseLong;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.messaging.support.MessageBuilder.withPayload;

@Component
public class CommandFormatter {

    private final JDA jda;

    @Autowired
    public CommandFormatter(final JDA jda) {
        this.jda = jda;
    }

    @Transformer
    public Message<net.dv8tion.jda.core.entities.Message> transform(final Message<net.dv8tion.jda.core.entities.Message> message) {

        final net.dv8tion.jda.core.entities.Message payload = message.getPayload();
        final String selfMention = jda.getSelfUser().getAsMention();
        final String newContent = payload.getContentRaw().replaceFirst(selfMention + " ?", "!");
        final MutableMessage newMessage = new MutableMessage(payload, newContent);

        return withPayload((net.dv8tion.jda.core.entities.Message) newMessage)
                .setHeader("args", getCommandArgs(payload.getContentRaw()))
                .build();
    }

    private static int getCommandArgs(final String commandString) {
        return commandString.replace("!", "").split(" ").length;
    }

    private boolean isOnlySelfMentioned(final net.dv8tion.jda.core.entities.Message payload) {
        final SelfUser selfUser = jda.getSelfUser();
        final List<Member> mentionedMembers = payload.getMentionedMembers();

        return mentionedMembers.size() == 1 &&
                mentionedMembers.stream()
                        .findFirst()
                        .get()
                        .getUser()
                        .getDiscriminator().equals(selfUser.getDiscriminator());
    }

    private class MutableMessage extends ReceivedMessage {

        public ReceivedMessage getAsReceivedMessage(final net.dv8tion.jda.core.entities.Message message,
                                                    final String newContent) {
            return new MutableMessage(message, newContent);
        }

        private MutableMessage(final net.dv8tion.jda.core.entities.Message message,
                               final String newContent) {
            super(parseLong(message.getId()),
                    message.getChannel(),
                    message.getType(),
                    message.isWebhookMessage(),
                    message.mentionsEveryone(),
                    message.isTTS(),
                    message.isPinned(),
                    isBlank(newContent) ? message.getContentRaw() : newContent,
                    message.getNonce(),
                    message.getAuthor(),
                    message.getEditedTime(),
                    message.getReactions(),
                    message.getAttachments(),
                    message.getEmbeds());
        }


    }

}
