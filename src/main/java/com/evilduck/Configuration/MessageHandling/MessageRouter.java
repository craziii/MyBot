package com.evilduck.Configuration.MessageHandling;

import com.evilduck.Entity.CommandDetail;
import com.evilduck.Repository.CommandDetailRepository;
import com.evilduck.Util.CommandHelper;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Router;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageRouter {

    private final CommandDetailRepository commandDetailRepository;
    private final CommandHelper commandHelper;


    @Autowired
    public MessageRouter(final CommandDetailRepository commandDetailRepository,
                         final CommandHelper commandHelper) {
        this.commandDetailRepository = commandDetailRepository;
        this.commandHelper = commandHelper;
    }

    @Router
    public String callableOrAutoFireCommandRouter(final org.springframework.messaging.Message<Message> message) {

        final String rawCommand = message.getPayload().getContentRaw();
        final List<CommandDetail> matchedCommands = commandHelper.matchCommandString(getCommandString(rawCommand));
        if (matchedCommands.size() == 1)
            return matchedCommands.get(0).getFullCommand() + "Channel";
        else if (matchedCommands.size() > 1) {
            final EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Your message matched these Commands");
            for (int i = 0; i < matchedCommands.size(); i++)
                embedBuilder.addField(String.valueOf(i), matchedCommands.get(i).getFullCommand(), true);
            embedBuilder.appendDescription("You can add more letters to your message to or type the whole command name to be more specific");

            message.getPayload().getTextChannel().sendMessage(embedBuilder.build()).queue();
            return null;
        } else
            return "autoFireCommandChannel";

    }

    private boolean callableCommandExists(String rawCommand) {
        return commandDetailRepository.findOneByFullCommand(rawCommand) != null;
    }

    private String getCommandString(final String rawCommand) {
        return rawCommand.replace("!", "").split(" ")[0];
    }


}
