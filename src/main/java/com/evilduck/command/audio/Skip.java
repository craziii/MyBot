package com.evilduck.command.audio;

import com.evilduck.command.interfaces.IsACommand;
import com.evilduck.command.interfaces.PrivateCommand;
import com.evilduck.configuration.audio.CacheableAudioPlayerProvider;
import com.evilduck.configuration.audio.TrackScheduler;
import com.evilduck.configuration.audio.TrackSchedulerProvider;
import com.evilduck.util.AudioPlayerSupport;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
@IsACommand(description = "Skips the currently playing song", tutorial = "Use !skip when a song is currently playing", aliases = {"s", "next"})
public class Skip implements PrivateCommand {

    private final AudioPlayerSupport audioPlayerSupport;
    private final TrackSchedulerProvider trackSchedulerProvider;
    private final CacheableAudioPlayerProvider audioPlayerProvider;

    @Autowired
    public Skip(final AudioPlayerSupport audioPlayerSupport,
                final TrackSchedulerProvider trackSchedulerProvider,
                final CacheableAudioPlayerProvider audioPlayerProvider) {
        this.audioPlayerSupport = audioPlayerSupport;
        this.trackSchedulerProvider = trackSchedulerProvider;
        this.audioPlayerProvider = audioPlayerProvider;
    }

    @Override
    public boolean hasPermissionToRun(final Member requestingMember) {
        return false;
    }

    @Override
    @ServiceActivator(inputChannel = "skipChannel")
    public void execute(final Message message) {
        final AudioPlayer audioPlayer = audioPlayerProvider.getPlayerForGuild(message.getGuild().getId()).getPlayer();
        final TrackScheduler trackScheduler = trackSchedulerProvider.getAudioEventAdapter(message.getGuild().getId());
        audioPlayerSupport.next(message.getTextChannel(), audioPlayer, trackScheduler);
    }
}
