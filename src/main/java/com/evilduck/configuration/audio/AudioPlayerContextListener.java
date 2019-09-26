package com.evilduck.configuration.audio;

import com.evilduck.repository.AudioContextStateRepository;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import net.dv8tion.jda.core.entities.Guild;

public class AudioPlayerContextListener extends AudioEventAdapter {

    private final Guild guild;
    private boolean contextStarted;
    private final AudioPlayer audioPlayer;
    private final AudioContextStateRepository repository;

    public AudioPlayerContextListener(final Guild guild,
                                      final AudioPlayer audioPlayer,
                                      final AudioContextStateRepository repository) {
        this.guild = guild;
        this.contextStarted = false;
        this.audioPlayer = audioPlayer;
        this.repository = repository;
    }

    @Override
    public void onEvent(final AudioEvent event) {
        if (!contextStarted) {
            repository.findById(guild.getId())
                    .ifPresent(context -> audioPlayer.getPlayingTrack()
                            .setPosition(context.getCurrentTrack().getPosition()));
            contextStarted = true;
        }

    }

}
