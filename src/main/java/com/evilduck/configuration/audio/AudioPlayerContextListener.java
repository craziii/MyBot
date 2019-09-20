package com.evilduck.configuration.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import net.dv8tion.jda.core.entities.Guild;

public class AudioPlayerContextListener extends AudioEventAdapter {

    private final Guild guild;
    private final AudioPlayer audioPlayer;
    private final TrackScheduler trackScheduler;
    private final CacheableAudioContextProvider audioContextProvider;

    public AudioPlayerContextListener(final Guild guild,
                                      final AudioPlayer audioPlayer,
                                      final TrackScheduler trackScheduler,
                                      final CacheableAudioContextProvider audioContextProvider) {
        this.guild = guild;
        this.audioPlayer = audioPlayer;
        this.trackScheduler = trackScheduler;
        this.audioContextProvider = audioContextProvider;
    }

    @Override
    public void onEvent(AudioEvent event) {
        audioContextProvider.persistAudioContextStateForGuild(guild, audioPlayer, trackScheduler);
    }
}
