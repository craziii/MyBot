package com.evilduck.Command;

import com.evilduck.Command.Interface.IsACommand;
import com.evilduck.Command.Interface.ManualCommand;
import com.evilduck.Repository.BigDickRepository;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
@IsACommand
public class Penis implements ManualCommand {

    private final BigDickRepository bigDickRepository;

    @Autowired
    public Penis(final BigDickRepository bigDickRepository) {
        this.bigDickRepository = bigDickRepository;
    }

    @Override
    @ServiceActivator(inputChannel = "penisChannel")
    public void execute(org.springframework.messaging.Message<Message> message) {
        final int penisLength = bigDickRepository.findById(message.getPayload()
                .getAuthor().getId()).isPresent() ? 100 : 1;

        message.getPayload()
                .getTextChannel()
                .sendMessage(":eggplant: " +
                        message.getPayload().getAuthor().getAsMention() +
                        "'s penis length is " +
                        penisLength +
                        " inches long :3")
                .queue();
    }

    @Override
    public boolean hasPermissionToRun(Member requestingMember) {
        return false;
    }

    @Override
    public void onSuccess(Message message) {

    }

    @Override
    public void onFail(Throwable throwable) {

    }
}
