package com.evilduck;

import com.evilduck.configuration.audio.CacheableAudioContextProvider;
import com.evilduck.entity.CachableAudioContext;
import net.dv8tion.jda.core.JDA;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MusicContextPersister {

    private final JDA jda;
    private final CacheableAudioContextProvider audioContextProvider;

    public MusicContextPersister(final JDA jda,
                                 final CacheableAudioContextProvider audioContextProvider) {
        this.jda = jda;
        this.audioContextProvider = audioContextProvider;
    }

    @Scheduled(fixedRate = 10000L)
    public void persisMusicContexts() {
        jda.getGuilds().forEach(guild -> {
            final CachableAudioContext audioContextForGuild = audioContextProvider.getAudioContextForGuild(guild);
            if (audioContextForGuild.getPlayer().getPlayingTrack() == null) return;
            audioContextProvider.persistAudioContextStateForGuild(
                    guild,
                    audioContextForGuild.getPlayer(),
                    audioContextForGuild.getTrackScheduler());
        });
    }

}
