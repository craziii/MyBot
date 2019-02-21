package com.evilduck.Command.Audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import net.dv8tion.jda.core.MessageBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;

class PauseTest {

    private AudioPlayer audioPlayer;
    private Pause pause;

    @BeforeEach
    void setUp() {
        audioPlayer = new DefaultAudioPlayer(new DefaultAudioPlayerManager());
        pause = new Pause(audioPlayer);
    }

    @Test
    void shouldTogglePause() {
        audioPlayer.setPaused(false);
        pause.execute(new MessageBuilder()
                .append("STUFF")
                .build());
        assertTrue(audioPlayer.isPaused());
    }

}