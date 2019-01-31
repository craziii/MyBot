package com.evilduck.Configuration.CommandConfiguration;


import net.dv8tion.jda.core.entities.Member;


public interface GenericCommand extends AutoFireCommand {

    boolean hasPermissionToRun(final Member requestingMember);

    void onSuccess(final net.dv8tion.jda.core.entities.Message message);
    void onFail(final Throwable throwable);

}
