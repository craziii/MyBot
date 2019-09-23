package com.evilduck.entity;

import org.springframework.data.annotation.Id;

import java.util.List;

public class AudioContextState {

    @Id
    private String guildId;
    private String voiceChannelId;
    private TrackState currentTrack;
    private List<String> queueIds;

    public AudioContextState() {
    }

    public AudioContextState(final String guildId,
                             final String voiceChannelId,
                             final TrackState currentTrack,
                             final List<String> queueIds) {
        this.guildId = guildId;
        this.voiceChannelId = voiceChannelId;
        this.currentTrack = currentTrack;
        this.queueIds = queueIds;
    }

    public String getGuildId() {
        return guildId;
    }

    public String getVoiceChannelId() {
        return voiceChannelId;
    }

    public TrackState getCurrentTrack() {
        return currentTrack;
    }

    public List<String> getQueueIds() {
        return queueIds;
    }

    public void setGuildId(final String guildId) {
        this.guildId = guildId;
    }

    public void setVoiceChannelId(final String voiceChannelId) {
        this.voiceChannelId = voiceChannelId;
    }

    public void setCurrentTrack(final TrackState currentTrack) {
        this.currentTrack = currentTrack;
    }

    public void setQueueIds(final List<String> queueIds) {
        this.queueIds = queueIds;
    }

    public boolean addTrackId(final String id) {
        return queueIds.add(id);
    }

    public void removeTrackId(final String id) {
        queueIds.removeIf(trackId -> trackId.equals(id));
    }

    public static TrackState trackStateFor(final String id, final long position) {
        return new TrackState(id, position);
    }

    public static class TrackState {
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
