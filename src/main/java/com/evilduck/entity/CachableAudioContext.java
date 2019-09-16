package com.evilduck.entity;

import com.evilduck.configuration.audio.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.core.entities.Guild;

public class CachableAudioContext {

    private final Guild guildId;
    private final AudioPlayer player;
    private final TrackScheduler trackScheduler;

    public CachableAudioContext(final Guild guild,
                                final AudioPlayer player) {
        this.trackScheduler = new TrackScheduler(guild);
        player.setVolume(25);
        this.guildId = guild;
        this.player = player;
        this.player.addListener(trackScheduler);
    }

    public Guild getGuildId() {
        return guildId;
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public TrackScheduler getTrackScheduler() {
        return trackScheduler;
    }
}
