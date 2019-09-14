package com.evilduck.entity;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import org.springframework.data.annotation.Id;

public class CachableAudioPlayer {

    @Id
    private final String guildId;
    private final AudioPlayer player;

    public CachableAudioPlayer(final String guildId,
                               final AudioPlayer player) {
        player.setVolume(25);
        this.guildId = guildId;
        this.player = player;
    }

    public String getGuildId() {
        return guildId;
    }

    public AudioPlayer getPlayer() {
        return player;
    }
}
