package com.evilduck.Configuration.CommandConfiguration;


import net.dv8tion.jda.core.entities.Member;
import org.springframework.messaging.Message;

import java.io.IOException;


public interface GenericCommand {

    void execute(final Message<net.dv8tion.jda.core.entities.Message> message) throws IOException;

    boolean hasPermissionToRun(final Member requestingMember);


    void onSuccess(final net.dv8tion.jda.core.entities.Message message);
    void onFail(final Throwable throwable);

}
