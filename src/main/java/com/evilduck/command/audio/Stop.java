package com.evilduck.command.audio;

import com.evilduck.command.standards.IsACommand;
import com.evilduck.command.standards.PrivateCommand;
import com.evilduck.command.standards.UnstableCommand;
import com.evilduck.configuration.TrackScheduler;
import com.evilduck.util.CommandHelper;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@IsACommand
public class Stop implements PrivateCommand, UnstableCommand {

    private final CommandHelper commandHelper;
    private final AudioPlayer audioPlayer;
    private final TrackScheduler trackScheduler;

    @Autowired
    public Stop(final CommandHelper commandHelper,
                final AudioPlayer audioPlayer,
                final TrackScheduler trackScheduler) {
        this.commandHelper = commandHelper;
        this.audioPlayer = audioPlayer;
        this.trackScheduler = trackScheduler;
    }

    @Override
    public boolean hasPermissionToRun(final Member requestingMember) {
        return true;
    }

    @Override
    public void onSuccess(final Message message) {

    }

    @Override
    public void onFail(final Throwable throwable) {

    }

    @Override
    @ServiceActivator(inputChannel = "stopChannel")
    public void execute(final Message message) throws IOException {
        final List<String> args = commandHelper.getArgs(message.getContentRaw());
        if (args.size() > 0 && args.get(0).toLowerCase().matches("all")) trackScheduler.clear();
        audioPlayer.stopTrack();
    }

}
