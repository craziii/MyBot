package com.evilduck.Command.Audio;

import com.evilduck.Command.Interface.IsACommand;
import com.evilduck.Command.Interface.PrivateCommand;
import com.evilduck.Configuration.TrackScheduler;
import com.evilduck.Util.AudioPlayerSupport;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@IsACommand(aliases = {"next"})
public class Skip implements PrivateCommand {

    private final TrackScheduler trackScheduler;
    private final AudioPlayer audioPlayer;
    private final AudioPlayerSupport audioPlayerSupport;

    @Autowired
    public Skip(final TrackScheduler trackScheduler,
                final AudioPlayer audioPlayer,
                final AudioPlayerSupport audioPlayerSupport) {
        this.trackScheduler = trackScheduler;
        this.audioPlayer = audioPlayer;
        this.audioPlayerSupport = audioPlayerSupport;
    }

    @Override
    public boolean hasPermissionToRun(final Member requestingMember) {
        return false;
    }

    @Override
    @ServiceActivator(inputChannel = "skipChannel")
    public void execute(final Message message) throws IOException {
        audioPlayerSupport.next(message.getTextChannel());
    }
}
