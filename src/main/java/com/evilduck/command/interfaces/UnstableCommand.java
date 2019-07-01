package com.evilduck.command.interfaces;


import net.dv8tion.jda.core.entities.Message;

public interface UnstableCommand extends GenericCommand {

    void onSuccess(Message message);

    void onFail(Throwable throwable);

}
