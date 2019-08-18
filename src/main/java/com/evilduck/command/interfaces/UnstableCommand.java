package com.evilduck.command.interfaces;


import net.dv8tion.jda.core.entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface UnstableCommand extends GenericCommand {

    Logger LOGGER = LoggerFactory.getLogger(UnstableCommand.class);

    default void onSuccess(final Message message) {
        LOGGER.info("Command was successful from mesage: {}", message.getContentRaw());
    }

    default void onFail(final Throwable throwable) {
        LOGGER.warn("Command was unsuccessful! The following error occured: {}", throwable.getMessage());
    }

}
