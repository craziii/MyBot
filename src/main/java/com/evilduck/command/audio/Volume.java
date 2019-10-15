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

import static java.lang.Integer.parseInt;

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

        final int currentVolume = audioPlayer.getVolume();
        if (args.isEmpty()) {
            if (currentVolume == 69) {
                originChannel.sendMessage("Current volume is " + currentVolume + ". Nice.").queue();
            } else {
                originChannel.sendMessage("Current volume is " + currentVolume).queue();
            }
            return;
        }

        final String volumeString = args.get(0);
        final VolumeOperator volumeOperator = new VolumeOperator(args.size() == 2 ? args.get(0).charAt(0) : volumeString.charAt(0));
        final int operatedVolume = parseInt(volumeString.substring(1));

        if (volumeOperator.isAddition()) {
            audioPlayer.setVolume(getValidVolume(currentVolume + operatedVolume));
            if (currentVolume == 69) {
                originChannel.sendMessage("Volume increased by " + operatedVolume + " to " + audioPlayer.getVolume() + ". Nice.").queue();
            } else {
                originChannel.sendMessage("Volume increased by " + operatedVolume + " to " + audioPlayer.getVolume()).queue();
            }
        } else if (volumeOperator.isSubtraction()) {
            audioPlayer.setVolume(getValidVolume(currentVolume - operatedVolume));
            if (currentVolume == 69) {
                originChannel.sendMessage("Volume decreased by " + operatedVolume + " to " + audioPlayer.getVolume() + ". Nice.").queue();
            } else {
                originChannel.sendMessage("Volume decreased by " + operatedVolume + " to " + audioPlayer.getVolume()).queue();
            }
        } else if (volumeOperator.isDivision()) {
            if (operatedVolume <= 0) {
                originChannel.sendMessage("Please do not try to get me to divide by zero, it hurts my circuits").queue();
                return;
            }
            audioPlayer.setVolume(getValidVolume(currentVolume / operatedVolume));
            if (currentVolume == 69) {
                originChannel.sendMessage("Volume divided by " + operatedVolume + " to " + audioPlayer.getVolume() + ". Nice.").queue();
            } else {
                originChannel.sendMessage("Volume divided by " + operatedVolume + " to " + audioPlayer.getVolume()).queue();
            }
        } else if (volumeOperator.isMultiplication()) {
            if (currentVolume == 69) {
                originChannel.sendMessage("Nice try buddy! Current volume is " + audioPlayer.getVolume() + ". Nice.").queue();
            } else {
                originChannel.sendMessage("Nice try buddy! Current volume is " + audioPlayer.getVolume()).queue();
            }
        } else {
            audioPlayer.setVolume(getValidVolume(parseInt(volumeString)));
            if (currentVolume == 69) {
                originChannel.sendMessage("Volume set to " + audioPlayer.getVolume() + ". Nice.").queue();
            } else {
                originChannel.sendMessage("Volume set to " + audioPlayer.getVolume()).queue();
            }
        }

    }

    private static int getValidVolume(final int newVolume) {
        if (newVolume < 0) return 0;
        else return Math.min(newVolume, 100);
    }

    private static class VolumeOperator {
        private final char operator;

        private VolumeOperator(final char operator) {
            this.operator = operator;
        }

        boolean isAddition() {
            return operator == '+';
        }

        boolean isSubtraction() {
            return operator == '-';
        }

        boolean isDivision() {
            return operator == '/' || operator == '÷';
        }

        boolean isMultiplication() {
            return operator == '*' || operator == 'x';
        }

    }

}
