package com.evilduck.command.audio;

import com.evilduck.command.interfaces.IsACommand;
import com.evilduck.command.interfaces.PrivateCommand;
import com.evilduck.configuration.audio.CacheableAudioContextProvider;
import com.evilduck.configuration.audio.TrackScheduler;
import com.evilduck.entity.CachableAudioContext;
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
    private final CacheableAudioContextProvider audioContextProvider;

    @Autowired
    public Skip(final AudioPlayerSupport audioPlayerSupport,
                final CacheableAudioContextProvider audioContextProvider) {
        this.audioPlayerSupport = audioPlayerSupport;
        this.audioContextProvider = audioContextProvider;
    }

    @Override
    public boolean hasPermissionToRun(final Member requestingMember) {
        return false;
    }

    @Override
    @ServiceActivator(inputChannel = "skipChannel")
    public void execute(final Message message) {
        final CachableAudioContext audioContextForGuild = audioContextProvider.getAudioContextForGuild(message.getGuild().getId());
        final AudioPlayer audioPlayer = audioContextForGuild.getPlayer();
        final TrackScheduler trackScheduler = audioContextForGuild.getTrackScheduler();
        audioPlayerSupport.next(message.getTextChannel(), audioPlayer, trackScheduler);
    }
}
