package com.evilduck.Command.Interface;


import net.dv8tion.jda.core.entities.Message;

public interface UnstableCommand extends GenericCommand {

    void onSuccess(final Message message);

    void onFail(final Throwable throwable);

}
