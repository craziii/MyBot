package com.evilduck.configuration.audio;

import com.evilduck.entity.CachableAudioPlayer;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class CacheableAudioPlayerProvider {

    private final AudioPlayerManagerAccessor audioPlayerManagerAccessor;

    public CacheableAudioPlayerProvider(final AudioPlayerManagerAccessor audioPlayerManagerAccessor) {
        this.audioPlayerManagerAccessor = audioPlayerManagerAccessor;
    }

    @Cacheable(value = "audio_player")
    public CachableAudioPlayer getPlayerForGuild(final String guildId) {
        return new CachableAudioPlayer(guildId, audioPlayerManagerAccessor.getAudioPlayerManager().createPlayer());
    }

}
