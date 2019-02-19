package com.evilduck.Command.Interface;

import net.dv8tion.jda.core.entities.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public interface GenericCommand {

    void execute(final Message message) throws IOException;

}
