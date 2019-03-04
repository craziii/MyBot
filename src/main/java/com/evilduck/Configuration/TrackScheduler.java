package com.evilduck.Configuration;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class TrackScheduler extends AudioEventAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrackScheduler.class);
    private final AudioPlayer audioPlayer;
    private final BlockingQueue<AudioTrack> queue;

    @Autowired
    public TrackScheduler(final AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        audioPlayer.addListener(this);
        this.queue = new LinkedBlockingQueue<>();
    }

    @Override
    public void onEvent(final AudioEvent event) {

    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
        super.onPlayerPause(player);
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        super.onPlayerResume(player);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        super.onTrackStart(player, track);
    }

    @Override
    public void onTrackEnd(final AudioPlayer player,
                           final AudioTrack track,
                           final AudioTrackEndReason endReason) {
        LOGGER.info("Track has just finished!, Trying next track");
        audioPlayer.stopTrack();
        audioPlayer.startTrack(queue.poll(), false);
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

}
