package com.evilduck.configuration.audio;

import com.evilduck.util.AudioPlayerSupport;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AudioResultHandler implements AudioLoadResultHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AudioResultHandler.class);

    private final Guild guild;
    private final TextChannel textChannel;
    private final VoiceChannel voiceChannel;
    private final AudioPlayer audioPlayer;
    private final TrackScheduler trackScheduler;
    private final AudioPlayerSupport audioPlayerSupport;

    public AudioResultHandler(final Guild guild,
                              final TextChannel textChannel,
                              final VoiceChannel voiceChannel,
                              final AudioPlayer audioPlayer,
                              final TrackScheduler trackScheduler,
                              final AudioPlayerSupport audioPlayerSupport) {
        this.guild = guild;
        this.textChannel = textChannel;
        this.voiceChannel = voiceChannel;
        this.audioPlayer = audioPlayer;
        this.trackScheduler = trackScheduler;
        this.audioPlayerSupport = audioPlayerSupport;
    }

    @Override
    public void trackLoaded(final AudioTrack track) {
        LOGGER.info("Loaded Track: {}", track.getIdentifier());
        final AudioManager audioManager = guild.getAudioManager();
        audioManager.setSendingHandler(new AudioPlayerSendHandler(audioPlayer));
        audioManager.openAudioConnection(voiceChannel);
        audioPlayerSupport.play(track, audioPlayer, trackScheduler, textChannel);
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
        if (textChannel != null)
            textChannel.sendMessage("Failed to play track reason: ")
                    .append(exception.getLocalizedMessage())
                    .queue();
    }

}
