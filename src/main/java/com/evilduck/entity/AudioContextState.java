package com.evilduck.entity;

import org.springframework.data.annotation.Id;

import java.util.Queue;

public class AudioContextState {

    @Id
    private String guildId;
    private TrackState currentTrack;
    private Queue<String> queueIds;

    public AudioContextState() {
    }

    public AudioContextState(final String guildId,
                             final TrackState currentTrack,
                             final Queue<String> queueIds) {
        this.guildId = guildId;
        this.currentTrack = currentTrack;
        this.queueIds = queueIds;
    }

    public String getGuildId() {
        return guildId;
    }

    public TrackState getCurrentTrack() {
        return currentTrack;
    }

    public Queue<String> getQueueIds() {
        return queueIds;
    }

    public void setGuildId(final String guildId) {
        this.guildId = guildId;
    }

    public void setCurrentTrack(final TrackState currentTrack) {
        this.currentTrack = currentTrack;
    }

    public void setQueueIds(final Queue<String> queueIds) {
        this.queueIds = queueIds;
    }

    public boolean offerTrackId(final String id) {
        return queueIds.offer(id);
    }

    public String pollTrackId(final String id) {
        return queueIds.poll();
    }

    public static TrackState trackStateFor(final String id, final long position) {
        return new TrackState(id, position);
    }

    private static class TrackState {
        private final String id;
        private final long position;

        private TrackState(final String id,
                           final long position) {
            this.id = id;
            this.position = position;
        }

        public String getId() {
            return id;
        }

        public long getPosition() {
            return position;
        }
    }

}
