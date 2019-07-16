package com.evilduck.entity;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Guild;

import java.util.concurrent.LinkedBlockingQueue;

public class MusicPlayerSession {

    private final LinkedBlockingQueue<AudioTrack> queue;
    private final MusicPlayerContext musicPlayerContext;
    private final Guild guild;

    public MusicPlayerSession(final LinkedBlockingQueue<AudioTrack> queue,
                              final MusicPlayerContext musicPlayerContext,
                              final Guild guild) {
        this.queue = queue;
        this.musicPlayerContext = musicPlayerContext;
        this.guild = guild;
    }

    public LinkedBlockingQueue<AudioTrack> getQueue() {
        return queue;
    }

    public MusicPlayerContext getMusicPlayerContext() {
        return musicPlayerContext;
    }

    public Guild getGuild() {
        return guild;
    }
}
