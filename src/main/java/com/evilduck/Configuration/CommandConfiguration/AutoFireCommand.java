package com.evilduck.Configuration.CommandConfiguration;

import net.dv8tion.jda.core.entities.Message;

import java.io.IOException;

public interface AutoFireCommand {

    void execute(final org.springframework.messaging.Message<Message> message) throws IOException;


}
