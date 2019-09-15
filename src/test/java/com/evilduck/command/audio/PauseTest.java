package com.evilduck.command.audio;

import com.evilduck.configuration.audio.AudioPlayerManagerAccessor;
import com.evilduck.configuration.audio.CacheableAudioContextProvider;
import com.evilduck.entity.CachableAudioContext;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PauseTest {

    @Mock
    private Guild guild;

    @Mock
    private Message message;

    private AudioPlayer audioPlayer;

    @MockBean
    private AudioPlayerManagerAccessor audioPlayerManagerAccessor;

    @Mock
    private CacheableAudioContextProvider audioContextProvider;

    @Mock
    private CachableAudioContext cachableAudioContext;

    @InjectMocks
    private Pause pause;

    @Before
    public void setUp() {
        audioPlayer = new DefaultAudioPlayer(new DefaultAudioPlayerManager());
        when(message.getGuild()).thenReturn(guild);
        when(audioContextProvider.getAudioContextForGuild(guild)).thenReturn(cachableAudioContext);
        when(cachableAudioContext.getPlayer()).thenReturn(audioPlayer);
        pause = new Pause(audioContextProvider);
    }

    @Test
    public void shouldTogglePause() {
        audioPlayer.setPaused(false);
        when(message.getGuild()).thenReturn(guild);
        when(guild.getId()).thenReturn("GUILD_ID");
        pause.execute(message);
        assertTrue(audioPlayer.isPaused());
    }

}