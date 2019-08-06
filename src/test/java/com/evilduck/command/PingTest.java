package com.evilduck.command;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.requests.restaction.MessageAction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PingTest extends CommandTest {

    @Mock
    private MessageAction messageAction;

    @Mock
    private JDA jda;

    @InjectMocks
    private Ping ping;

    @Test
    public void shouldTrySendPingMessage() {
        when(message.getJDA()).thenReturn(jda);
        when(jda.getPing()).thenReturn(0L);
        when(message.getTextChannel()).thenReturn(textChannel);
        when(textChannel.sendMessage(any(Message.class))).thenReturn(messageAction);

        ping.execute(message);
        verify(textChannel).sendMessage(Mockito.any(Message.class));
    }

    @Test
    public void hasPermissionToRun() {
    }
}