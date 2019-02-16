package com.evilduck.Configuration;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AudioResultHandler implements AudioLoadResultHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AudioResultHandler.class);

    private final Message message;
    private final VoiceChannel voiceChannel;
    private final AudioPlayer audioPlayer;
    private final TrackScheduler trackScheduler;

    public AudioResultHandler(final Message message,
                              final VoiceChannel voiceChannel,
                              final AudioPlayer audioPlayer,
                              final TrackScheduler trackScheduler) {
        this.message = message;
        this.voiceChannel = voiceChannel;
        this.audioPlayer = audioPlayer;
        this.trackScheduler = trackScheduler;
    }

    @Override
    public void trackLoaded(final AudioTrack track) {
        LOGGER.info("Loaded Track: {}", track.getIdentifier());
        final AudioManager audioManager = message.getTextChannel()
                .getGuild()
                .getAudioManager();
        audioManager.setSendingHandler(new AudioPlayerSendHandler(audioPlayer));
        audioManager.openAudioConnection(voiceChannel);
        trackScheduler.queue(track);
    }

    @Override
    public void playlistLoaded(final AudioPlaylist playlist) {

    }

    @Override
    public void noMatches() {

    }

    @Override
    public void loadFailed(final FriendlyException exception) {

    }
}
