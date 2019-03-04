package com.evilduck.Command.Audio;

import com.evilduck.Command.Interface.IsACommand;
import com.evilduck.Command.Interface.PublicCommand;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@IsACommand(aliases = "q", description = "Displays all audio tracks as a queue")
public class Queue implements PublicCommand {

    @Override
    public void execute(final Message message) throws IOException {

    }
}
