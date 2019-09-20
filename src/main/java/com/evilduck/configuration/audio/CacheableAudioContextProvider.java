package com.evilduck.configuration.audio;

import com.evilduck.entity.AudioContextState;
import com.evilduck.entity.CachableAudioContext;
import com.evilduck.repository.AudioContextStateRepository;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Guild;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;

import static com.evilduck.entity.AudioContextState.trackStateFor;
import static java.util.stream.Collectors.toCollection;

@Component
public class CacheableAudioContextProvider {

    private final AudioPlayerManagerAccessor audioPlayerManagerAccessor;
    private final AudioContextStateRepository audioContextStateRepository;

    public CacheableAudioContextProvider(final AudioPlayerManagerAccessor audioPlayerManagerAccessor,
                                         final AudioContextStateRepository audioContextStateRepository) {
        this.audioPlayerManagerAccessor = audioPlayerManagerAccessor;
        this.audioContextStateRepository = audioContextStateRepository;
    }

    @Cacheable(value = "audio_player", key = "#guild.getId()")
    public CachableAudioContext getAudioContextForGuild(final Guild guild) {
        final AudioPlayer player = audioPlayerManagerAccessor.getAudioPlayerManager().createPlayer();
        final CachableAudioContext cachableAudioContext = new CachableAudioContext(guild, player);
        player.addListener(new AudioPlayerContextListener(guild, cachableAudioContext.getPlayer(), cachableAudioContext.getTrackScheduler(), this));
        return cachableAudioContext;
    }

    public void persistAudioContextStateForGuild(final Guild guild,
                                                 final AudioPlayer audioPlayer,
                                                 final TrackScheduler trackScheduler) {
        if (trackScheduler.queueLength() == 0 && audioPlayer.getPlayingTrack() == null) return;
        final AudioTrack playingTrack = audioPlayer.getPlayingTrack();
        final long currentPosition = playingTrack.getPosition();
        final LinkedBlockingQueue<String> trackIds = trackScheduler
                .getQueue()
                .stream()
                .map(AudioTrack::getIdentifier)
                .collect(toCollection(LinkedBlockingQueue::new));

        final AudioContextState currentState = new AudioContextState(guild.getId(),
                trackStateFor(playingTrack.getIdentifier(), currentPosition),
                trackIds);

        audioContextStateRepository.save(currentState);
    }

}
