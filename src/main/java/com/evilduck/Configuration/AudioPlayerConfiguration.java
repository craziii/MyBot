package com.evilduck.Configuration;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AudioPlayerConfiguration {

    @Bean
    public AudioPlayerManager audioPlayerManager() {
        final DefaultAudioPlayerManager defaultAudioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(defaultAudioPlayerManager);
        return defaultAudioPlayerManager;
    }

    @Bean
    public AudioPlayer audioPlayer(final AudioPlayerManager audioPlayerManager) {
        return audioPlayerManager.createPlayer();
    }

    @Bean
    public TrackScheduler trackScheduler(final AudioPlayer audioPlayer) {
        final TrackScheduler trackScheduler = new TrackScheduler(audioPlayer);
        audioPlayer.addListener(trackScheduler);
        return trackScheduler;
    }

}
