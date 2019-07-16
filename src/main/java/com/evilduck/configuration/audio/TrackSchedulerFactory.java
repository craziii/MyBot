package com.evilduck.configuration.audio;

import com.evilduck.entity.MusicPlayerSession;

public class TrackSchedulerFactory {

    public TrackScheduler create(final MusicPlayerSession session) {
        return new TrackScheduler(session);
    }

}
