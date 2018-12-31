package com.evilduck.evilduck.Action;

import com.evilduck.evilduck.Command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class Ping implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(Ping.class);

    @ServiceActivator(inputChannel = "pingChannel")
    public void execute(final Message<net.dv8tion.jda.core.entities.Message> message) {
        LOGGER.info("Recieved: {}", message.getPayload().getContentRaw());
    }
}
