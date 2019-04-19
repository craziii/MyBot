package com.evilduck.command.standards;

import net.dv8tion.jda.core.entities.Member;

public interface PrivateCommand extends GenericCommand {

    boolean hasPermissionToRun(Member requestingMember);

}
