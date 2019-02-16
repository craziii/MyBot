package com.evilduck.Configuration;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class TrackScheduler implements AudioEventListener {

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

    public void queue(final AudioTrack audioTrack) {
        if (!audioPlayer.startTrack(audioTrack, true))
            queue.offer(audioTrack);
    }

    public void clear() {
        queue.clear();
    }

}
