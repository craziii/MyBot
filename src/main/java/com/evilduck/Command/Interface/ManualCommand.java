package com.evilduck.Command.Interface;


import net.dv8tion.jda.core.entities.Member;
import org.springframework.stereotype.Component;

@Component
public interface ManualCommand extends GenericCommand {

    boolean hasPermissionToRun(final Member requestingMember);

    void onSuccess(final net.dv8tion.jda.core.entities.Message message);

    void onFail(final Throwable throwable);

}
