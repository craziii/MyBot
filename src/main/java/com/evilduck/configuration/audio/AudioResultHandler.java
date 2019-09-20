package com.evilduck.configuration.audio;

import com.evilduck.util.AudioPlayerSupport;
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
    private final AudioPlayerSupport audioPlayerSupport;

    public AudioResultHandler(final Message message,
                              final VoiceChannel voiceChannel,
                              final AudioPlayer audioPlayer,
                              final TrackScheduler trackScheduler,
                              final AudioPlayerSupport audioPlayerSupport) {
        this.message = message;
        this.voiceChannel = voiceChannel;
        this.audioPlayer = audioPlayer;
        this.trackScheduler = trackScheduler;
        this.audioPlayerSupport = audioPlayerSupport;
    }

    @Override
    public void trackLoaded(final AudioTrack track) {
        LOGGER.info("Loaded Track: {}", track.getIdentifier());
        final AudioManager audioManager = message.getTextChannel().getGuild().getAudioManager();
        audioManager.setSendingHandler(new AudioPlayerSendHandler(audioPlayer));
        audioManager.openAudioConnection(voiceChannel);
        audioPlayerSupport.play(track, audioPlayer, trackScheduler, message.getTextChannel());
        audioPlayerSupport.updateAudioContextForGuild(message.getGuild());
    }


    @Override
    public void playlistLoaded(final AudioPlaylist playlist) {
        LOGGER.info("Loaded Playlist {}, containing {} tracks",
                playlist.isSearchResult() ? "Search" : playlist.getName(), playlist.getTracks().size());
        trackLoaded(playlist.getTracks().get(0));
        // TODO: Get playlists working properly! Not search results though!
    }

    @Override
    public void noMatches() {
        final String stuff = "stuff";
    }

    @Override
    public void loadFailed(final FriendlyException exception) {
        LOGGER.info("Track Failed: {}", exception.toString());
        message.getTextChannel()
                .sendMessage("Failed to play track reason: ")
                .append(exception.getLocalizedMessage())
                .queue();
    }

}
