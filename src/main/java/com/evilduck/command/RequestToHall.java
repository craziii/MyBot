package com.evilduck.command;

import com.evilduck.command.interfaces.PublicCommand;
import com.evilduck.util.CommandHelper;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.List;

//@Component
//@IsACommand(aliases = {"r", "request"})
public class RequestToHall implements PublicCommand {

    private final JDA jda;
    private final CommandHelper commandHelper;

    @Autowired
    public RequestToHall(final JDA jda,
                         final CommandHelper commandHelper) {
        this.jda = jda;
        this.commandHelper = commandHelper;
    }

    @Override
    @ServiceActivator(inputChannel = "requestToHallChannel")
    public void execute(final Message message) {
        final List<String> args = commandHelper.getArgs(message.getContentRaw());
        if (args.isEmpty()) message.getTextChannel().sendMessage("You must specify a link to play!").queue();

        jda.getUserById("131611023974203392").openPrivateChannel().queue(privateChannel ->
                privateChannel.sendMessage("Time to get to work! Request the following song on the bot: " + args.get(0))
                        .queue());


    }

}
