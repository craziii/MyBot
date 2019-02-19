package com.evilduck.Command.Audio;

import com.evilduck.Command.Interface.IsACommand;
import com.evilduck.Command.Interface.ManualCommand;
import com.evilduck.Configuration.AudioResultHandler;
import com.evilduck.Configuration.TrackScheduler;
import com.evilduck.Exception.UserNotInVoiceChannelException;
import com.evilduck.Util.CommandHelper;
import com.jecklgamis.util.Try;
import com.jecklgamis.util.TryFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@IsACommand
public class Play implements ManualCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(Play.class);

    private final AudioPlayerManager audioPlayerManager;
    private final AudioPlayer audioPlayer;
    private final TrackScheduler trackScheduler;
    private final CommandHelper commandHelper;

    public Play(final AudioPlayerManager audioPlayerManager,
                final AudioPlayer audioPlayer,
                final TrackScheduler trackScheduler,
                final CommandHelper commandHelper) {
        this.audioPlayerManager = audioPlayerManager;
        this.audioPlayer = audioPlayer;
        this.trackScheduler = trackScheduler;
        this.commandHelper = commandHelper;
    }

    @Override
    public boolean hasPermissionToRun(final Member requestingMember) {
        return false;
    }

    @Override
    public void onSuccess(final Message message) {

    }

    @Override
    public void onFail(final Throwable throwable) {

    }

    @Override
    @ServiceActivator(inputChannel = "playChannel")
    public void execute(final Message message) throws IOException {

        final TextChannel originChannel = message.getTextChannel();
        final List<String> args = commandHelper.getArgs(message.getContentRaw());
        if (args.size() < 2) {
            originChannel.sendMessage("You must specify a link to play!").queue();
            return;
        }

        final Try<VoiceChannel> voiceChannelTry = getVoiceChannelByUserId(
                message.getAuthor().getId(),
                message.getGuild().getVoiceChannels());
        if (voiceChannelTry.isFailure()) {
            originChannel.sendMessage("I couldn't find you in any of the voice channels!").queue();
            return;
        }

        startPlayFromLink(message, args, voiceChannelTry);
    }

    private void startPlayFromLink(final Message message,
                                   final List<String> args,
                                   final Try<VoiceChannel> voiceChannelTry) {
        audioPlayerManager.loadItem(args.get(1),
                new AudioResultHandler(message,
                        voiceChannelTry.get(),
                        audioPlayer,
                        trackScheduler));
    }

    private static Try<VoiceChannel> getVoiceChannelByUserId(String id, List<VoiceChannel> voiceChannels) {
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
