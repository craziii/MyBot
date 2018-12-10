package com.evilduck.evilduck;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        System.out.printf("Received message from author: %s : %s",
                event.getAuthor().getName(),
                event.getMessage().getContentDisplay());
        if (event.getMessage().getContentRaw().equals("!ping"))
            event.getChannel().sendMessage("Lol Isaac is the big gay! 8======D").queue();
    }

}
