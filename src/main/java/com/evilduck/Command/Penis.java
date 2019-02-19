package com.evilduck.Command;

import com.evilduck.Command.Interface.IsACommand;
import com.evilduck.Command.Interface.PublicCommand;
import com.evilduck.Command.Interface.UnstableCommand;
import com.evilduck.Repository.BigDickRepository;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
@IsACommand
public class Penis implements PublicCommand, UnstableCommand {

    private final BigDickRepository bigDickRepository;

    @Autowired
    public Penis(final BigDickRepository bigDickRepository) {
        this.bigDickRepository = bigDickRepository;
    }

    @Override
    @ServiceActivator(inputChannel = "penisChannel")
    public void execute(Message message) {
        final int penisLength = bigDickRepository.findById(
                message.getAuthor().getId()).isPresent() ? 100 : 1;

        message.getTextChannel()
                .sendMessage(":eggplant: " +
                        message.getAuthor().getAsMention() +
                        "'s penis length is " +
                        penisLength +
                        " inches long :3")
                .queue();
    }

    @Override
    public void onSuccess(Message message) {

    }

    @Override
    public void onFail(Throwable throwable) {

    }
}
