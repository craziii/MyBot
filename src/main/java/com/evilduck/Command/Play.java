package com.evilduck.Command;

import com.evilduck.Command.Interface.IsACommand;
import com.evilduck.Command.Interface.ManualCommand;
import com.evilduck.Configuration.AudioPlayerSendHandler;
import com.evilduck.Configuration.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;
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
    private final JDA jda;

    public Play(final AudioPlayerManager audioPlayerManager,
                final AudioPlayer audioPlayer,
                final TrackScheduler trackScheduler,
                final JDA jda) {
        this.audioPlayerManager = audioPlayerManager;
        this.audioPlayer = audioPlayer;
        this.trackScheduler = trackScheduler;
        this.jda = jda;
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
    public void execute(final org.springframework.messaging.Message<Message> message) throws IOException {
        audioPlayerManager.loadItem("https://www.youtube.com/watch?v=QgydTdThoeA&ab_channel=SamO%27NellaAcademy",
                new AudioLoadResultHandler() {
                    @Override
                    public void trackLoaded(AudioTrack track) {
                        LOGGER.info("Loaded Track");
                        final Guild guild = message.getPayload().getGuild();
                        final AudioManager audioManager = message.getPayload().getTextChannel().getGuild().getAudioManager();
                        audioManager.setSendingHandler(new AudioPlayerSendHandler(audioPlayer));
                        final List<VoiceChannel> voiceChannels = guild.getVoiceChannels();
                        for (VoiceChannel voiceChannel : voiceChannels) {
                            audioManager.openAudioConnection(voiceChannel);
                        }
                        trackScheduler.queue(track);
//                        audioPlayer.playTrack(track);
                    }

                    @Override
                    public void playlistLoaded(AudioPlaylist playlist) {
                        LOGGER.info("Loaded Playlist");
                    }

                    @Override
                    public void noMatches() {
                        LOGGER.info("No Matches");
                    }

                    @Override
                    public void loadFailed(FriendlyException exception) {
                        LOGGER.info("Failed to Load Track");
                    }
                });
    }

}
