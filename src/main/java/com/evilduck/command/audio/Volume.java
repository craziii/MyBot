package com.evilduck.command.audio;

import com.evilduck.command.interfaces.IsACommand;
import com.evilduck.command.interfaces.PrivateCommand;
import com.evilduck.configuration.audio.CacheableAudioContextProvider;
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
@IsACommand(description = "Displays or edits the volume output from the bot", tutorial = "Use !volume to display the volume or e.g. \"!volume 69\" to change the volume ", aliases = "v")
public class Volume implements PrivateCommand {

    private static final String DEANS_SERVER = "203522480063643658";

    private final CacheableAudioContextProvider audioPlayerProvider;
    private final CommandHelper commandHelper;

    @Autowired
    public Volume(final CacheableAudioContextProvider audioPlayerProvider,
                  final CommandHelper commandHelper) {
        this.audioPlayerProvider = audioPlayerProvider;
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
        final AudioPlayer audioPlayer = audioPlayerProvider.getAudioContextForGuild(message.getGuild()).getPlayer();

        if (args.isEmpty()) {
            originChannel.sendMessage("Current volume is " + audioPlayer.getVolume()).queue();
            return;
        }

        final int volume = Integer.parseInt(args.get(0));
        if (message.getGuild().getId().matches(DEANS_SERVER) && Math.abs(volume) < 200) audioPlayer.setVolume(volume);
        else if (volume < 0 || volume > 100) originChannel.sendMessage("Volume must be between 0 and 100!").queue();
        else audioPlayer.setVolume(volume);
    }

}
