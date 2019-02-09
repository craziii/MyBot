package com.evilduck.Command.Interface;

import net.dv8tion.jda.core.entities.Message;

public interface AutoFire {

    boolean execute(final org.springframework.messaging.Message<Message> message);

}
