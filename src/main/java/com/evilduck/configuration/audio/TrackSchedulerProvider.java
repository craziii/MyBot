package com.evilduck.configuration.audio;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class TrackSchedulerProvider {

    @Cacheable(value = "track_scheduler")
    public TrackScheduler getAudioEventAdapter(final String guildId) {
        return new TrackScheduler(guildId);
    }

}
