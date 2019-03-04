package com.evilduck.Util;

import com.evilduck.Configuration.AudioResultHandler;
import com.evilduck.Configuration.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    private void startPlayFromLink(final Message message,
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

    public void play(final AudioTrack audioTrack, final TextChannel textChannel) {
        if (!audioPlayer.startTrack(audioTrack, true)) trackScheduler.offer(audioTrack);
        displayPlayingTrack(audioTrack, textChannel);
    }

    private static void displayPlayingTrack(final AudioTrack audioTrack,
                                            final TextChannel textChannel) {
        textChannel.sendMessage(audioTrack == null ?
                new EmbedBuilder().setTitle("The queue is empty")
                        .appendDescription("play some more stuff!")
                        .build() :
                new EmbedBuilder().setTitle("Queued Track")
                        .addField("Title", audioTrack.getInfo().title, false)
                        .addField("Duration", String.valueOf(audioTrack.getDuration()), false)
                        .build())
                .queue();
    }

}
