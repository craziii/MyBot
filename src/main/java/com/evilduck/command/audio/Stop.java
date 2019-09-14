package com.evilduck.command.audio;

import com.evilduck.command.interfaces.IsACommand;
import com.evilduck.command.interfaces.PrivateCommand;
import com.evilduck.command.interfaces.UnstableCommand;
import com.evilduck.configuration.audio.CacheableAudioPlayerProvider;
import com.evilduck.configuration.audio.TrackScheduler;
import com.evilduck.configuration.audio.TrackSchedulerProvider;
import com.evilduck.util.CommandHelper;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@IsACommand(description = "Stops the bot playing music", tutorial = "Use !stop when the bot is playing music")
public class Stop implements PrivateCommand, UnstableCommand {

    private final CommandHelper commandHelper;
    private final TrackSchedulerProvider trackSchedulerProvider;
    private final CacheableAudioPlayerProvider audioPlayerProvider;

    @Autowired
    public Stop(final CommandHelper commandHelper,
                final TrackSchedulerProvider trackSchedulerProvider,
                final CacheableAudioPlayerProvider audioPlayerProvider) {
        this.commandHelper = commandHelper;
        this.trackSchedulerProvider = trackSchedulerProvider;
        this.audioPlayerProvider = audioPlayerProvider;
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
    public void execute(final Message message) {
        final List<String> args = commandHelper.getArgs(message.getContentRaw());
        final TrackScheduler trackScheduler = trackSchedulerProvider.getAudioEventAdapter(message.getGuild().getId());
        if (args.size() > 0 && args.get(0).toLowerCase().matches("all")) trackScheduler.clear();
        final AudioPlayer audioPlayer = audioPlayerProvider.getPlayerForGuild(message.getGuild().getId()).getPlayer();
        audioPlayer.stopTrack();
    }

}
