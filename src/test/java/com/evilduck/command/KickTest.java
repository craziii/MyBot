package com.evilduck.command;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.requests.restaction.AuditableRestAction;
import net.dv8tion.jda.core.requests.restaction.MessageAction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KickTest extends CommandTest {

    @Mock
    private Member selfMember;

    @Mock
    private Member mentionedMember;

    @Mock
    private Guild guild;

    @Mock
    private GuildController controller;

    @Mock
    private AuditableRestAction<Void> auditableRestAction;

    @Mock
    private MessageAction messageAction;

    @Mock
    private User mentionedUser;

    @InjectMocks
    private Kick kick;

    @Test
    public void testKicksMember() {
        setupMocks("TESTNAME", true, true);
        kick.execute(message);
        verify(controller, times(1)).kick(mentionedMember);
    }

    @Test(expected = PermissionException.class)
    public void noKickPermission() {
        setupMocks("TESTNAME", false, true);
        kick.execute(message);
    }

    @Test(expected = PermissionException.class)
    public void cantKickUser() {
        setupMocks("TESTNAME", true, false);
        kick.execute(message);
    }

    @Test(expected = PermissionException.class)
    public void creatorProtection() {
        setupMocks("EvilDuck", true, true);
        kick.execute(message);
    }

    private void setupMocks(final String memberName,
                            final boolean hasPermissionToKick,
                            final boolean canKickMember) {
        when(message.getTextChannel()).thenReturn(textChannel);
        when(textChannel.sendMessage(anyString())).thenReturn(messageAction);
        when(message.getMentionedUsers()).thenReturn(singletonList(mentionedUser));
        when(message.getMentionedMembers()).thenReturn(singletonList(mentionedMember));
        when(guild.getMember(any(User.class))).thenReturn(mentionedMember);
        when(mentionedMember.getUser()).thenReturn(mentionedUser);
        when(mentionedUser.getName()).thenReturn(memberName);
        when(message.getGuild()).thenReturn(guild);
        when(message.getMember()).thenReturn(selfMember);
        when(guild.getSelfMember()).thenReturn(selfMember);
        when(selfMember.hasPermission(Permission.KICK_MEMBERS)).thenReturn(hasPermissionToKick);
        when(selfMember.canInteract(any(Member.class))).thenReturn(canKickMember);
        when(guild.getController()).thenReturn(controller);
        when(controller.kick(any(Member.class))).thenReturn(auditableRestAction);
    }

}