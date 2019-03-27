package com.evilduck.Command.Audio;

import com.evilduck.Command.Interface.IsACommand;
import com.evilduck.Command.Interface.PrivateCommand;
import com.evilduck.Util.CommandHelper;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@IsACommand(aliases = "v")
public class Volume implements PrivateCommand {

    private final AudioPlayer audioPlayer;
    private final CommandHelper commandHelper;

    @Autowired
    public Volume(final AudioPlayer audioPlayer,
                  final CommandHelper commandHelper) {
        this.audioPlayer = audioPlayer;
        this.commandHelper = commandHelper;
    }

    @Override
    public boolean hasPermissionToRun(final Member requestingMember) {
        return false;
    }

    @Override
    @ServiceActivator(inputChannel = "volumeChannel")
    public void execute(final Message message) {
        final List<String> args = commandHelper.getArgs(message.getContentRaw());
        final TextChannel originChannel = message.getTextChannel();

        if (args.size() < 2) originChannel.sendMessage("You have not specified a new volume!").queue();

        final int volume = Integer.parseInt(args.get(0));
        if (volume < 0 || volume > 100) originChannel.sendMessage("Volume must be between 0 and 100!").queue();

        audioPlayer.setVolume(volume);
    }

}
