package com.evilduck.Configuration.MessageHandling;

import net.dv8tion.jda.core.entities.Message;

import java.io.IOException;

public interface GenericCommand {

    void execute(final org.springframework.messaging.Message<Message> message) throws IOException;


}
