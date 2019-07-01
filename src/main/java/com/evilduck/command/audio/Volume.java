package com.evilduck.command.audio;

import com.evilduck.command.interfaces.IsACommand;
import com.evilduck.command.interfaces.PrivateCommand;
import com.evilduck.util.CommandHelper;
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

    private static final String DEANS_SERVER = "203522480063643658";

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

        if (args.isEmpty()) {
            originChannel.sendMessage("You have not specified a new volume!").queue();
            return;
        }

        final int volume = Integer.parseInt(args.get(0));

        if (message.getGuild().getId().matches(DEANS_SERVER)) audioPlayer.setVolume(volume);
        else if (volume < 0 || volume > 100) originChannel.sendMessage("Volume must be between 0 and 100!").queue();
        else audioPlayer.setVolume(volume);
    }

}
