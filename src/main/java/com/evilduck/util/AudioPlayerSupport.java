package com.evilduck.util;

import com.evilduck.configuration.AudioResultHandler;
import com.evilduck.configuration.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

@Component
public class AudioPlayerSupport {

    private final AudioPlayerManager audioPlayerManager;
    private final AudioPlayer audioPlayer;
    private final TrackScheduler trackScheduler;

    @Autowired
    public AudioPlayerSupport(final AudioPlayerManager audioPlayerManager,
                              final AudioPlayer audioPlayer,
                              final TrackScheduler trackScheduler) {
        this.audioPlayerManager = audioPlayerManager;
        this.audioPlayer = audioPlayer;
        this.trackScheduler = trackScheduler;
    }

    public void startPlayFromLink(final Message message,
                                  final String url,
                                  final VoiceChannel voiceChannelTry) {
        audioPlayerManager.loadItem(url, new AudioResultHandler(
                message,
                voiceChannelTry,
                audioPlayer,
                this));
    }


    public void next(final TextChannel textChannel) {
        audioPlayer.stopTrack();
        final AudioTrack nextTrack = trackScheduler.getNextTrack();
        audioPlayer.startTrack(nextTrack, true);
        displayPlayingTrack(nextTrack, textChannel);
    }

    public void play(final AudioTrack audioTrack,
                     final TextChannel textChannel) {
        if (!audioPlayer.startTrack(audioTrack, true)) trackScheduler.offer(audioTrack);
        displayPlayingTrack(audioTrack, textChannel);
    }

    private static void displayPlayingTrack(final AudioTrack audioTrack,
                                            final TextChannel textChannel) {
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