package com.evilduck.command.audio;

import com.evilduck.command.interfaces.GenericCommand;
import com.evilduck.command.interfaces.IsACommand;
import com.evilduck.command.interfaces.UnstableCommand;
import com.evilduck.configuration.audio.AudioPlayerManagerAccessor;
import com.evilduck.configuration.audio.AudioResultHandler;
import com.evilduck.configuration.audio.CacheableAudioContextProvider;
import com.evilduck.configuration.audio.TrackScheduler;
import com.evilduck.entity.CachableAudioContext;
import com.evilduck.exception.UserNotInVoiceChannelException;
import com.evilduck.util.AudioPlayerSupport;
import com.evilduck.util.CommandHelper;
import com.jecklgamis.util.Try;
import com.jecklgamis.util.TryFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@IsACommand(
        description = "Starts up a new song or resumes a song",
        tutorial = "Use with a link to queue a new song or use it without a link to resume playing the queue",
        aliases = {
                "p",
                "start"
        }
)
public class Play implements GenericCommand, UnstableCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(Play.class);

    private final CommandHelper commandHelper;
    private final AudioPlayerSupport audioPlayerSupport;
    private final CacheableAudioContextProvider audioPlayerProvider;
    private final AudioPlayerManagerAccessor audioPlayerManagerAccessor;

    public Play(final CommandHelper commandHelper,
                final AudioPlayerSupport audioPlayerSupport,
                final CacheableAudioContextProvider audioPlayerProvider,
                final AudioPlayerManagerAccessor audioPlayerManagerAccessor) {
        this.commandHelper = commandHelper;
        this.audioPlayerSupport = audioPlayerSupport;
        this.audioPlayerProvider = audioPlayerProvider;
        this.audioPlayerManagerAccessor = audioPlayerManagerAccessor;
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
        final CachableAudioContext audioContextForGuild = audioPlayerProvider.getAudioContextForGuild(message.getGuild().getId());
        final AudioPlayer audioPlayer = audioContextForGuild.getPlayer();
        if (args.isEmpty() && audioPlayer.isPaused()) {
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
            final TrackScheduler trackScheduler = audioContextForGuild.getTrackScheduler();
            startPlayFromLink(message, commandHelper.getArgsAsAString(args, 0), voiceChannelTry.get(), audioPlayer, trackScheduler);
        }
    }

    private void startPlayFromLink(final Message message,
                                   final String search,
                                   final VoiceChannel voiceChannelTry,
                                   final AudioPlayer audioPlayer,
                                   final TrackScheduler trackScheduler) {
        final boolean searchIsUri = search.matches(".*youtube\\.com/.*");
        final String searchPrefix = searchIsUri ? "" : "ytsearch: ";
        final AudioPlayerManager audioPlayerManager = audioPlayerManagerAccessor.getAudioPlayerManager();
        final AudioResultHandler resultHandler = new AudioResultHandler(message, voiceChannelTry, audioPlayer, trackScheduler, audioPlayerSupport);
        audioPlayerManager.loadItem((searchPrefix + search), resultHandler);

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
