package com.evilduck.Configuration.CommandConfiguration;


import org.springframework.messaging.Message;

import java.io.IOException;
import java.net.MalformedURLException;


public interface GenericCommand {

    void execute(final Message<net.dv8tion.jda.core.entities.Message> message) throws IOException;

    void onSuccess(final net.dv8tion.jda.core.entities.Message message);
    void onFail(final Throwable throwable);

}
