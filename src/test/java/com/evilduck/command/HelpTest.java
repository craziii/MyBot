package com.evilduck.command;

import com.evilduck.entity.CommandDetail;
import com.evilduck.repository.CommandDetailRepository;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.requests.restaction.MessageAction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HelpTest extends CommandTest {

    @Mock
    private CommandDetailRepository repository;

    @InjectMocks
    private Help help;

    @Test
    public void testHelp() throws IOException {
        final CommandDetail commandDetail = new CommandDetail("test");
        commandDetail.setDescription("description");
        commandDetail.setTutorial("tutorial");
        when(repository.findAll()).thenReturn(Collections.singletonList(commandDetail));
        when(message.getTextChannel()).thenReturn(textChannel);
        when(textChannel.sendMessage(any(MessageEmbed.class))).thenReturn(mock(MessageAction.class));
        help.execute(message);
        verify(textChannel, times(1)).sendMessage(any(MessageEmbed.class));
    }

}