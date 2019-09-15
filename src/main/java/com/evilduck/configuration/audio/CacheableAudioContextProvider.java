package com.evilduck.configuration.audio;

import com.evilduck.entity.CachableAudioContext;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.core.entities.Guild;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class CacheableAudioContextProvider {

    private final AudioPlayerManagerAccessor audioPlayerManagerAccessor;

    public CacheableAudioContextProvider(final AudioPlayerManagerAccessor audioPlayerManagerAccessor) {
        this.audioPlayerManagerAccessor = audioPlayerManagerAccessor;
    }

    @Cacheable(value = "audio_player", key = "#guild.getId()")
    public CachableAudioContext getAudioContextForGuild(final Guild guild) {
        final AudioPlayer player = audioPlayerManagerAccessor.getAudioPlayerManager().createPlayer();
        return new CachableAudioContext(guild, player);
    }

}
