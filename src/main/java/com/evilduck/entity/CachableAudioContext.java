package com.evilduck.entity;

import com.evilduck.configuration.audio.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import org.springframework.data.annotation.Id;

public class CachableAudioContext {

    @Id
    private final String guildId;
    private final AudioPlayer player;
    private final TrackScheduler trackScheduler;

    public CachableAudioContext(final String guildId,
                                final AudioPlayer player) {
        this.trackScheduler = new TrackScheduler(guildId);
        player.setVolume(25);
        this.guildId = guildId;
        this.player = player;
        this.player.addListener(trackScheduler);
    }

    public String getGuildId() {
        return guildId;
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public TrackScheduler getTrackScheduler() {
        return trackScheduler;
    }
}
