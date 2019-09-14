package com.evilduck.configuration.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AudioPlayerConfiguration {

    //    @Bean
    public AudioPlayerManager audioPlayerManager() {
        final AudioPlayerManager defaultAudioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(defaultAudioPlayerManager);
        return defaultAudioPlayerManager;
    }

//    @Bean
//    public AudioEventAdapter audioEventAdapter(final AudioPlayer audioPlayer) {
//        final AudioEventAdapter audioEventAdapter = new TrackScheduler(guildId);
//        audioPlayer.addListener(audioEventAdapter);
//        return audioEventAdapter;
//    }
//
//    @Bean
//    public TrackScheduler trackScheduler(final AudioEventAdapter audioEventAdapter) {
//        return (TrackScheduler) audioEventAdapter;
//    }


}
