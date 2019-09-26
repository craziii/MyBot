package com.evilduck.configuration.audio;

import com.evilduck.entity.AudioContextState;
import com.evilduck.entity.CachableAudioContext;
import com.evilduck.repository.AudioContextStateRepository;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.evilduck.entity.AudioContextState.trackStateFor;

@Service
@CacheConfig(cacheNames = {"audio_context"})
public class CacheableAudioContextProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheableAudioContextProvider.class);

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
        player.addListener(new AudioPlayerContextListener(guild, player, audioContextStateRepository));
        return new CachableAudioContext(guild, player);
    }

    public void persistAudioContextStateForGuild(final Guild guild,
                                                 final AudioPlayer audioPlayer,
                                                 final TrackScheduler trackScheduler) {
        if (trackScheduler.queueLength() == 0 && audioPlayer.getPlayingTrack() == null) return;
        final AudioTrack playingTrack = audioPlayer.getPlayingTrack();
        final long currentPosition = playingTrack.getPosition();
        final List<String> trackIds = trackScheduler
                .getQueue()
                .stream()
                .map(AudioTrack::getIdentifier)
                .collect(Collectors.toList());
        final AudioManager audioManager = guild.getAudioManager();
        final VoiceChannel voiceChannel = Optional.ofNullable(audioManager.getConnectedChannel())
                .orElse(audioManager.getQueuedAudioConnection());
        final AudioContextState currentState = new AudioContextState(guild.getId(),
                voiceChannel.getId(),
                trackStateFor(playingTrack.getIdentifier(), currentPosition),
                trackIds);
        audioContextStateRepository.save(currentState);
    }

}
