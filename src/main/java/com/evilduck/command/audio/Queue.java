package com.evilduck.command.audio;

import com.evilduck.command.standards.IsACommand;
import com.evilduck.command.standards.PublicCommand;
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
