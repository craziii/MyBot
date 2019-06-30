package com.evilduck.command.audio;

import com.evilduck.command.standards.GenericCommand;
import com.evilduck.command.standards.IsACommand;
import com.evilduck.command.standards.UnstableCommand;
import com.evilduck.exception.UserNotInVoiceChannelException;
import com.evilduck.util.AudioPlayerSupport;
import com.evilduck.util.CommandHelper;
import com.jecklgamis.util.Try;
import com.jecklgamis.util.TryFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@IsACommand(aliases = {"p", "start"})
public class Play implements GenericCommand, UnstableCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(Play.class);

    private final AudioPlayer audioPlayer;
    private final CommandHelper commandHelper;
    private final AudioPlayerSupport audioPlayerSupport;

    public Play(final AudioPlayer audioPlayer,
                final CommandHelper commandHelper,
                final AudioPlayerSupport audioPlayerSupport) {
        this.audioPlayer = audioPlayer;
        this.commandHelper = commandHelper;
        this.audioPlayerSupport = audioPlayerSupport;

//        audioPlayer.addListener(trackScheduler);
    }

    @Override
    public void onSuccess(final Message message) {

    }

    @Override
    public void onFail(final Throwable throwable) {

    }

    @Override
    @ServiceActivator(inputChannel = "playChannel")
    public void execute(final Message message) {
        final TextChannel originChannel = message.getTextChannel();
        final List<String> args = commandHelper.getArgs(message.getContentRaw());
        if (audioPlayer.isPaused()) {
            audioPlayer.setPaused(false);
            originChannel.sendMessage("Resuming: " + audioPlayer.getPlayingTrack().getInfo().title).queue();
        } else if (args.isEmpty()) {
            originChannel.sendMessage("You must specify a link to play!").queue();
        } else {

            final Try<VoiceChannel> voiceChannelTry = getVoiceChannelByUserId(
                    message.getAuthor().getId(),
                    message.getGuild().getVoiceChannels());
            if (voiceChannelTry.isFailure()) {
                originChannel.sendMessage("I couldn't find you in any of the voice channels!").queue();
                return;
            }
            startPlayFromLink(message, commandHelper.getArgsAsAString(args, 0), voiceChannelTry.get());
        }
    }

    private void startPlayFromLink(final Message message,
                                   final String search,
                                   final VoiceChannel voiceChannelTry) {
        final boolean searchIsUri = search.matches(".*youtube\\.com/.*");
        final String searchPrefix = searchIsUri ? "" : "ytsearch: ";

        audioPlayerSupport.startPlayFromLink(message, searchPrefix.concat(search), voiceChannelTry);

    }

    private static Try<VoiceChannel> getVoiceChannelByUserId(final String id,
                                                             final List<VoiceChannel> voiceChannels) {
        return TryFactory.attempt(() -> voiceChannels.stream()
                .filter(vc -> vc.getMembers()
                        .stream()
                        .anyMatch(m -> m.getUser()
                                .getId()
                                .equals(id)))
                .findFirst()
                .orElseThrow(() -> new UserNotInVoiceChannelException("User " +
                        id +
                        " not found in any voice channels!")));
    }

}
