package com.evilduck;

import com.evilduck.configuration.audio.AudioPlayerManagerAccessor;
import com.evilduck.configuration.audio.AudioResultHandler;
import com.evilduck.configuration.audio.CacheableAudioContextProvider;
import com.evilduck.configuration.audio.TrackScheduler;
import com.evilduck.entity.AudioContextState;
import com.evilduck.entity.CachableAudioContext;
import com.evilduck.repository.AudioContextStateRepository;
import com.evilduck.util.AudioPlayerSupport;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class MusicRestarter implements ApplicationListener<ApplicationReadyEvent> {

    private final JDA jda;
    private final AudioPlayerSupport audioPlayerSupport;
    private final AudioContextStateRepository repository;
    private final CacheableAudioContextProvider audioContextProvider;
    private final AudioPlayerManagerAccessor playerManagerAccessor;


    public MusicRestarter(final JDA jda,
                          final AudioPlayerSupport audioPlayerSupport,
                          final AudioContextStateRepository repository,
                          final CacheableAudioContextProvider audioContextProvider,
                          final AudioPlayerManagerAccessor playerManagerAccessor) {
        this.jda = jda;
        this.audioPlayerSupport = audioPlayerSupport;
        this.repository = repository;
        this.audioContextProvider = audioContextProvider;
        this.playerManagerAccessor = playerManagerAccessor;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        restartAllGuilds();
    }

    public void restartAllGuilds() {
        jda.getGuilds().forEach(guild -> repository.findById(guild.getId())
                .ifPresent(audioContextState -> {
                    final CachableAudioContext audioContextForGuild = audioContextProvider.getAudioContextForGuild(guild);
                    final AudioPlayer player = audioContextForGuild.getPlayer();
                    final TrackScheduler trackScheduler = audioContextForGuild.getTrackScheduler();
                    restartTrackFromContext(
                            guild,
                            audioContextState,
                            player,
                            trackScheduler,
                            audioContextState.getCurrentTrack().getId());
                    audioContextState.getQueueIds().forEach(id ->
                            restartTrackFromContext(guild, audioContextState, player, trackScheduler, id));
                }));
    }

    @PreDestroy
    public void finishHooks() {
        jda.getGuilds().forEach(guild -> {
            final CachableAudioContext audioContextForGuild = audioContextProvider.getAudioContextForGuild(guild);
            audioContextProvider.persistAudioContextStateForGuild(
                    guild,
                    audioContextForGuild.getPlayer(),
                    audioContextForGuild.getTrackScheduler());
        });
    }


    private void restartTrackFromContext(final Guild guild,
                                         final AudioContextState audioContextState,
                                         final AudioPlayer player,
                                         final TrackScheduler trackScheduler,
                                         final String trackId) {
        final AudioResultHandler resultHandler = new AudioResultHandler(guild, null, guild
                .getVoiceChannelById(audioContextState.getVoiceChannelId()), player, trackScheduler, audioPlayerSupport);
        playerManagerAccessor.getAudioPlayerManager()
                .loadItem("https://www.youtube.com/watch?v=" + trackId, resultHandler);
    }
}
