package com.evilduck.Command;

import com.evilduck.Command.Interface.IsACommand;
import com.evilduck.Command.Interface.PublicCommand;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@IsACommand
public class Poll implements PublicCommand {

    @Override
    public void execute(Message message) throws IOException {

    }
}
