package com.evilduck.Command.Interface;

import net.dv8tion.jda.core.entities.Message;
import org.springframework.stereotype.Component;

@Component
public interface AutoFire {

    boolean execute(final org.springframework.messaging.Message<Message> message);

}
