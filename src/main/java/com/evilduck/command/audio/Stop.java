package com.evilduck.command.audio;

import com.evilduck.command.interfaces.IsACommand;
import com.evilduck.command.interfaces.PrivateCommand;
import com.evilduck.command.interfaces.UnstableCommand;
import com.evilduck.configuration.audio.CacheableAudioContextProvider;
import com.evilduck.configuration.audio.TrackScheduler;
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
    private final CacheableAudioContextProvider audioContextProvider;

    @Autowired
    public Stop(final CommandHelper commandHelper,
                final CacheableAudioContextProvider audioContextProvider) {
        this.commandHelper = commandHelper;
        this.audioContextProvider = audioContextProvider;
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
        final TrackScheduler trackScheduler = audioContextProvider.getAudioContextForGuild(message.getGuild().getId()).getTrackScheduler();
        if (args.size() > 0 && args.get(0).toLowerCase().matches("all")) trackScheduler.clear();
        final AudioPlayer audioPlayer = audioContextProvider.getAudioContextForGuild(message.getGuild().getId()).getPlayer();
        audioPlayer.stopTrack();
    }

}
