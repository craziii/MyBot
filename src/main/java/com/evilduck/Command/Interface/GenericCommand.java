package com.evilduck.Command.Interface;

import net.dv8tion.jda.core.entities.Message;

import java.io.IOException;

public interface GenericCommand {

    void execute(final Message message) throws IOException;

}
