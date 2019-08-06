package com.evilduck.command;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import org.mockito.Mock;


/**
 * Adds mocks for Message with a TextChannel and
 * Member to all be injected into a Command
 */
public abstract class CommandTest {

    @Mock protected Message message;
    @Mock protected TextChannel textChannel;
    @Mock protected Member member;

}
