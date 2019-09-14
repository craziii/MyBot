package com.evilduck.configuration.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import org.springframework.stereotype.Component;

@Component
public class AudioPlayerManagerAccessor {

    private final AudioPlayerManager audioPlayerManager;

    public AudioPlayerManagerAccessor() {
        final AudioPlayerManager defaultAudioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(defaultAudioPlayerManager);

        this.audioPlayerManager = defaultAudioPlayerManager;
    }

    public AudioPlayerManager getAudioPlayerManager() {
        return audioPlayerManager;
    }

}
