package com.evilduck.command.interfaces;

import net.dv8tion.jda.core.entities.Message;

import java.io.IOException;

public interface GenericCommand {

    void execute(Message message) throws IOException;

}
