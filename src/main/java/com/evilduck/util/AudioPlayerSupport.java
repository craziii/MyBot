package com.evilduck.util;

import com.evilduck.configuration.audio.CacheableAudioContextProvider;
import com.evilduck.configuration.audio.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class AudioPlayerSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(AudioPlayerSupport.class);

    private final CacheableAudioContextProvider audioContextProvider;

    public AudioPlayerSupport(final CacheableAudioContextProvider audioContextProvider) {
        this.audioContextProvider = audioContextProvider;
    }

    public void play(final AudioTrack audioTrack,
                     final AudioPlayer audioPlayer,
                     final TrackScheduler trackScheduler,
                     final TextChannel textChannel) {
        if (isEmptyTrack(textChannel, audioTrack)) return;
        if (!audioPlayer.startTrack(audioTrack, true)) trackScheduler.offer(audioTrack);
        displayPlayingTrack(audioTrack, textChannel);
    }

    public void next(final TextChannel textChannel,
                     final AudioPlayer audioPlayer,
                     final TrackScheduler trackScheduler) {
        final AudioTrack nextTrack = trackScheduler.getNextTrack();
        if (audioPlayer.startTrack(nextTrack, false)) displayPlayingTrack(nextTrack, textChannel);
    }

    private boolean isEmptyTrack(final TextChannel textChannel,
                                 final AudioTrack nextTrack) {
        if (nextTrack == null) {
            textChannel.getGuild().getAudioManager().closeAudioConnection();
            return true;
        }
        return false;
    }

    public String getTimeFormattedString(final long duration) {
        final long hours = duration / 3600000;
        final long minutes = (duration / 60000) % 60;
        final long seconds = (duration / 1000) % 60;
        return hours > 0 ? format("%d:%02d:%02d", hours, minutes, seconds) :
                format("%d:%02d", minutes, seconds);
    }

    private void displayPlayingTrack(final AudioTrack audioTrack,
                                     final TextChannel textChannel) {
        if (textChannel == null) return;
        if (audioTrack == null) {
            LOGGER.warn("Tried to display null audio track!");
        } else {
            final long duration = audioTrack.getDuration();

            textChannel.sendMessage(new EmbedBuilder().setTitle("Queued Track")
                    .addField("Title", audioTrack.getInfo().title, false)
                    .addField("Origin", audioTrack.getInfo().uri, false)
                    .addField("Duration", getTimeFormattedString(duration),
                            false)
                    .build())
                    .queue();
        }
    }

}
