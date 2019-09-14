package com.evilduck.configuration.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.player.event.PlayerPauseEvent;
import com.sedmelluq.discord.lavaplayer.player.event.PlayerResumeEvent;
import com.sedmelluq.discord.lavaplayer.player.event.TrackEndEvent;
import com.sedmelluq.discord.lavaplayer.player.event.TrackStartEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;

import java.util.concurrent.LinkedBlockingQueue;


public class TrackScheduler extends AudioEventAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrackScheduler.class);

    @Id
    private final String guildId;
    private final LinkedBlockingQueue<AudioTrack> queue;

    public TrackScheduler(final String guildId) {
        this.guildId = guildId;
        this.queue = new LinkedBlockingQueue<>();
    }

    @Override
    public void onEvent(final AudioEvent event) {
        final AudioPlayer player = event.player;
        if (event instanceof TrackStartEvent) {
            onTrackStart(player, ((TrackStartEvent) event).track);
        } else if (event instanceof TrackEndEvent) {
            onTrackEnd(player, ((TrackEndEvent) event).track, ((TrackEndEvent) event).endReason);
        } else if (event instanceof PlayerResumeEvent) {
            onPlayerResume(player);
        } else if (event instanceof PlayerPauseEvent) {
            onPlayerPause(player);
        } else {
            LOGGER.error("An undefined AudioEvent has occurred! {}", event.toString());
        }
    }

    @Override
    public void onPlayerPause(final AudioPlayer player) {
        super.onPlayerPause(player);
    }

    @Override
    public void onPlayerResume(final AudioPlayer player) {
        super.onPlayerResume(player);
    }

    @Override
    public void onTrackStart(final AudioPlayer player,
                             final AudioTrack track) {
        super.onTrackStart(player, track);
    }

    @Override
    public void onTrackEnd(final AudioPlayer player,
                           final AudioTrack track,
                           final AudioTrackEndReason endReason) {
        LOGGER.info("Track has just finished!, Trying next track");
        player.startTrack(getNextTrack(), true);
        super.onTrackEnd(player, track, endReason);
    }

    public void offer(final AudioTrack audioTrack) {
        queue.offer(audioTrack);
    }

    public AudioTrack getNextTrack() {
        return queue.poll();
    }

    public void clear() {
        queue.clear();
    }

    public int queueLength() {
        return queue.size();
    }

    public LinkedBlockingQueue<AudioTrack> getQueue() {
        return queue;
    }

    public String getGuildId() {
        return guildId;
    }
}
