package com.evilduck.configuration.message.handling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.stereotype.Component;

@Component
public class ErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);

    // TODO: WHY DOESN'T THIS FUCKER WORK???
    @ServiceActivator(inputChannel = "errorChannel")
    public void errorHandler(final Message<MessageHandlingException> exception) {
        LOGGER.info("Error processing message!");
        LOGGER.info("exception: Type: {}, Message: {}",
                exception.getPayload().getCause(), exception.getPayload().getMessage());
    }

}
