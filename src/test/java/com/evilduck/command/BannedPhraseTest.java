package com.evilduck.command;

import com.evilduck.entity.BannedPhraseEntity;
import com.evilduck.repository.BannedPhraseRepository;
import com.evilduck.util.CommandHelper;
import net.dv8tion.jda.core.requests.restaction.MessageAction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BannedPhraseTest extends CommandTest {

    @Mock
    private CommandHelper commandHelper;

    @Mock
    private BannedPhraseRepository repository;

    @InjectMocks
    private BannedPhrase bannedPhrase;

    @Test
    public void hasPermissionToRun() {
        givenAMessage("save test");
        when(member.hasPermission(anyList())).thenReturn(false);
        bannedPhrase.execute(message);
        verify(textChannel, times(1)).sendMessage("You do not have permission to edit banned phrases!");
    }

    @Test
    public void executeSave() {
        givenAMessage("save test");
        when(member.hasPermission(anyList())).thenReturn(true);
        bannedPhrase.execute(message);
        verify(repository, times(1))
                .save(any(BannedPhraseEntity.class));

    }

    @Test
    public void executeInsert() {
        givenAMessage("insert test");
        when(member.hasPermission(anyList())).thenReturn(true);
        bannedPhrase.execute(message);
        verify(repository, times(1))
                .save(any(BannedPhraseEntity.class));

    }

    @Test
    public void executeRemove() {
        givenAMessage("remove test");
        when(member.hasPermission(anyList())).thenReturn(true);
        bannedPhrase.execute(message);
        verify(repository, times(1))
                .delete(any(BannedPhraseEntity.class));

    }

    @Test
    public void executeDelete() {
        givenAMessage("delete test");
        when(member.hasPermission(anyList())).thenReturn(true);
        bannedPhrase.execute(message);
        verify(repository, times(1))
                .delete(any(BannedPhraseEntity.class));

    }

    private void givenAMessage(final String rawContent) {
        when(message.getTextChannel()).thenReturn(textChannel);
        when(textChannel.sendMessage(anyString())).thenReturn(mock(MessageAction.class));
        when(message.getMember()).thenReturn(member);
        when(message.getContentRaw()).thenReturn(rawContent);
        final String[] rawContentSplit = rawContent.trim().split(" ");
        when(commandHelper.getArgs(anyString())).thenReturn(asList(rawContentSplit));
        when(commandHelper.getArgsAsString(anyString(), anyInt())).thenReturn(rawContentSplit[0]);

    }
}