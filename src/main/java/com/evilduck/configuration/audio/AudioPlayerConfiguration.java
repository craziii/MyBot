package com.evilduck.configuration.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AudioPlayerConfiguration {

    @Bean
    public AudioPlayerManager audioPlayerManager() {
        final AudioPlayerManager defaultAudioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(defaultAudioPlayerManager);
        return defaultAudioPlayerManager;
    }

    @Bean
    public AudioPlayer audioPlayer(final AudioPlayerManager audioPlayerManager) {
        final AudioPlayer player = audioPlayerManager.createPlayer();
        player.setVolume(25);
        return player;
    }

    @Bean
    public AudioEventAdapter audioEventAdapter(final AudioPlayer audioPlayer) {
        final AudioEventAdapter audioEventAdapter = new TrackScheduler(musicPlayerSession);
        audioPlayer.addListener(audioEventAdapter);
        return audioEventAdapter;
    }

    @Bean
    public TrackScheduler trackScheduler(final AudioEventAdapter audioEventAdapter) {
        return (TrackScheduler) audioEventAdapter;
    }


}
