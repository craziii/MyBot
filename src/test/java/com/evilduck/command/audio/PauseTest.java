package com.evilduck.command.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import net.dv8tion.jda.core.MessageBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PauseTest {

    private AudioPlayer audioPlayer;
    private Pause pause;

    @Before
    public void setUp() {
        audioPlayer = new DefaultAudioPlayer(new DefaultAudioPlayerManager());
        pause = new Pause(audioPlayer);
    }

    @Test
    public void shouldTogglePause() {
        audioPlayer.setPaused(false);
        pause.execute(new MessageBuilder()
                .append("STUFF")
                .build());
        assertTrue(audioPlayer.isPaused());
    }

}