package com.evilduck.util;

import com.evilduck.configuration.audio.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static java.lang.String.format;
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
        if (isEmptyTrack(textChannel, audioTrack)) return;
        if (!audioPlayer.startTrack(audioTrack, true)) trackScheduler.offer(audioTrack);
        displayPlayingTrack(audioTrack, textChannel);
    }

    public void next(final TextChannel textChannel,
                     final AudioPlayer audioPlayer,
                     final TrackScheduler trackScheduler) {
        final AudioTrack nextTrack = trackScheduler.getNextTrack();
        if (!audioPlayer.startTrack(nextTrack, false)) textChannel.sendMessage("I had a problem playing the next track!").queue();
        else displayPlayingTrack(nextTrack, textChannel);
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
        final long hours = HOURS.convert(duration, MILLISECONDS);
        final long minutes = MINUTES.convert(duration, MILLISECONDS) - (60 * hours);
        final long seconds = SECONDS.convert(duration, MILLISECONDS) - (60 * minutes);
        return hours > 1 ? format("%d:%02d:%02d", hours, minutes, seconds) :
                format("%d:%02d", minutes, seconds);
    }

    private void displayPlayingTrack(final AudioTrack audioTrack,
                                     final TextChannel textChannel) {
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
