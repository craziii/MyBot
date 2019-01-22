package com.evilduck.Command;

import com.evilduck.Configuration.CommandConfiguration.GenericCommand;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
public class Penis implements GenericCommand {

    @Override
    @ServiceActivator(inputChannel = "penisChannel")
    public void execute(org.springframework.messaging.Message<Message> message) {
        final TextChannel originChannel = message.getPayload().getTextChannel();
        final int penisLength = message.getPayload().getAuthor().getName().matches("DuckChan|Unstable Sloth") ? 100 : 1;

        originChannel.sendMessage(":eggplant: Your Penis length is " + penisLength + " inches long :3").queue();
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFail() {

    }
}
