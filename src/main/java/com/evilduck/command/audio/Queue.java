package com.evilduck.command.audio;

import com.evilduck.command.interfaces.IsACommand;
import com.evilduck.command.interfaces.PublicCommand;
import com.evilduck.configuration.audio.CacheableAudioContextProvider;
import com.evilduck.configuration.audio.TrackScheduler;
import com.evilduck.entity.CachableAudioContext;
import com.evilduck.util.AudioPlayerSupport;
import com.evilduck.util.CommandHelper;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.String.format;

@Component
@IsACommand(
        aliases = "q",
        description = "Displays all songs as a queue",
        tutorial = "Use !queue")
public class Queue implements PublicCommand {

    private final CacheableAudioContextProvider audioContextProvider;
    private final AudioPlayerSupport audioPlayerSupport;
    private final CommandHelper commandHelper;

    @Autowired
    public Queue(final CacheableAudioContextProvider audioContextProvider,
                 final AudioPlayerSupport audioPlayerSupport,
                 final CommandHelper commandHelper) {
        this.audioContextProvider = audioContextProvider;
        this.audioPlayerSupport = audioPlayerSupport;
        this.commandHelper = commandHelper;
    }

    @Override
    @ServiceActivator(inputChannel = "queueChannel")
    public void execute(final Message message) {
        final List<String> args = commandHelper.getArgs(message.getContentRaw());
        final CachableAudioContext audioContextForGuild = audioContextProvider.getAudioContextForGuild(message.getGuild());
        final TrackScheduler trackScheduler = audioContextForGuild.getTrackScheduler();
        final TextChannel textChannel = message.getTextChannel();
        if (!args.isEmpty()) {
            if (args.get(0).toLowerCase().matches("clear|empty|dump")) {
                textChannel.sendMessage("Queue has been cleared!").queue();
                trackScheduler.clear();
            }
        } else {
            final LinkedBlockingQueue<AudioTrack> queue = trackScheduler.getQueue();
            final AudioTrack playingTrack = audioContextForGuild
                    .getPlayer()
                    .getPlayingTrack();
            if (playingTrack != null) {
                final EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.addField("Currently Playing", playingTrack.getInfo().title, false)
                        .addField("Current - Duration",
                                audioPlayerSupport.getTimeFormattedString(playingTrack.getPosition()) + " - " +
                                        audioPlayerSupport.getTimeFormattedString(playingTrack.getDuration()), false)
                        .addBlankField(false);
                final String trackUri = playingTrack.getInfo().uri;
                embedBuilder.setThumbnail(trackUri.contains("youtu.be") || trackUri.contains("youtube") ?
                        format("https://img.youtube.com/vi/%s/0.jpg", playingTrack.getInfo().identifier) : null);
                populateQueueEmbed(queue, embedBuilder);
                embedBuilder.setTitle("Current Queue");
                textChannel.sendMessage(embedBuilder.build()).queue();
            } else if (queue.isEmpty()) textChannel.sendMessage("The queue is empty, maybe play something I dunno").queue();
        }
    }

    private void populateQueueEmbed(final LinkedBlockingQueue<AudioTrack> queue,
                                    final EmbedBuilder embedBuilder) {
        int i = 2;
        for (final AudioTrack audioTrack : queue) {
            embedBuilder.addField(i == 2 ? "Up Next" : "Track " + i, audioTrack.getInfo().title, false)
                    .addField("Duration", audioPlayerSupport.getTimeFormattedString(audioTrack.getDuration()), false)
                    .addBlankField(false);
            i++;
        }
    }

}
