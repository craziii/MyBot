package com.evilduck.evilduck.Command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

@Component
public class Ping {

    private static final Logger LOGGER = LoggerFactory.getLogger(Ping.class);

    private final MessageChannel pingChannel;

    public Ping(final MessageChannel pingChannel) {
        this.pingChannel = pingChannel;
    }

    @ServiceActivator(inputChannel = "pingChannel")
    public void execute(final Message<String> message) {
        final String command = message.getPayload();
        LOGGER.info("Recieved: {}\n", command);
    }
}
