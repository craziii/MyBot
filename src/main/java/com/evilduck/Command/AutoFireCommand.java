package com.evilduck.Command;

import com.evilduck.Command.Interface.GenericCommand;
import com.evilduck.Command.Interface.IsACommand;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@IsACommand(callable = false)
public class AutoFireCommand implements GenericCommand {

    @Override
    public void execute(final org.springframework.messaging.Message<Message> message) throws IOException {


    }
}
