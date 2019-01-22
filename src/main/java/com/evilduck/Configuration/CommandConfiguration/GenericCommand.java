package com.evilduck.Configuration.CommandConfiguration;


import org.springframework.messaging.Message;


public interface GenericCommand {

    void execute(final Message<net.dv8tion.jda.core.entities.Message> message);

    void onSuccess(final net.dv8tion.jda.core.entities.Message message);
    void onFail(final Throwable throwable);

}
