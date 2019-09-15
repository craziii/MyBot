package com.evilduck.configuration.audio;

import com.evilduck.entity.CachableAudioContext;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class CacheableAudioContextProvider {

    private final AudioPlayerManagerAccessor audioPlayerManagerAccessor;

    public CacheableAudioContextProvider(final AudioPlayerManagerAccessor audioPlayerManagerAccessor) {
        this.audioPlayerManagerAccessor = audioPlayerManagerAccessor;
    }

    @Cacheable(value = "audio_player")
    public CachableAudioContext getAudioContextForGuild(final String guildId) {
        final AudioPlayer player = audioPlayerManagerAccessor.getAudioPlayerManager().createPlayer();
        return new CachableAudioContext(guildId, player);
    }

}
