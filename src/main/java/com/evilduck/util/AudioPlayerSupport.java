package com.evilduck.util;

import com.evilduck.configuration.audio.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

@Component
public class AudioPlayerSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(AudioPlayerSupport.class);

    public void play(final AudioTrack audioTrack,
                     final AudioPlayer audioPlayer,
                     final TrackScheduler trackScheduler,
                     final TextChannel textChannel) {
        if (isEmptyTrack(textChannel, audioTrack)) {
            return;
        }
        if (!audioPlayer.startTrack(audioTrack, true)) trackScheduler.offer(audioTrack);
        displayPlayingTrack(audioTrack, textChannel);
    }

    public void next(final TextChannel textChannel,
                     final AudioPlayer audioPlayer,
                     final TrackScheduler trackScheduler) {
        audioPlayer.stopTrack();
        final AudioTrack nextTrack = trackScheduler.getNextTrack();
        if (isEmptyTrack(textChannel, nextTrack)) return;
        audioPlayer.playTrack(nextTrack);
        displayPlayingTrack(nextTrack, textChannel);
    }

    private boolean isEmptyTrack(final TextChannel textChannel,
                                 final AudioTrack nextTrack) {
        if (nextTrack == null) {
            textChannel.getGuild().getAudioManager().closeAudioConnection();
            return true;
        }
        return false;
    }

    private static void displayPlayingTrack(final AudioTrack audioTrack,
                                            final TextChannel textChannel) {
        if (audioTrack == null) {
            LOGGER.warn("Tried to display null audio track!");
        } else {
            final long duration = audioTrack.getDuration();
            final long hours = HOURS.convert(duration, MILLISECONDS);
            final long minutes = MINUTES.convert(duration, MILLISECONDS) - (60 * hours);
            final long seconds = SECONDS.convert(duration, MILLISECONDS) - (60 * minutes);

            textChannel.sendMessage(new EmbedBuilder().setTitle("Queued Track")
                    .addField("Title", audioTrack.getInfo().title, false)
                    .addField("Origin", audioTrack.getInfo().uri, false)
                    .addField("Duration",
                            hours > 1 ?
                                    String.format("%d:%d:%d", hours, minutes, seconds) :
                                    String.format("%d:%d", minutes, seconds), false)
                    .build())
                    .queue();
        }
    }

}
