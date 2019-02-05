package com.evilduck.Configuration.MessageHandling;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

@MessagingGateway(name = "commandGateway", defaultRequestChannel = "commandInputChannel")
public interface MessageGateway {

    @Gateway
    void processCommand(final Message<net.dv8tion.jda.core.entities.Message> message);

}
