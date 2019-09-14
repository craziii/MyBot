package com.evilduck.command.audio;

import com.evilduck.command.interfaces.IsACommand;
import com.evilduck.command.interfaces.PublicCommand;
import com.evilduck.configuration.audio.CacheableAudioPlayerProvider;
import com.evilduck.configuration.audio.TrackScheduler;
import com.evilduck.configuration.audio.TrackSchedulerProvider;
import com.evilduck.util.CommandHelper;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

@Component
@IsACommand(
        aliases = "q",
        description = "Displays all songs as a queue",
        tutorial = "Use !queue")
public class Queue implements PublicCommand {

    private final CacheableAudioPlayerProvider audioPlayerProvider;
    private final TrackSchedulerProvider trackSchedulerProvider;
    private final CommandHelper commandHelper;

    @Autowired
    public Queue(final CacheableAudioPlayerProvider audioPlayerProvider,
                 final TrackSchedulerProvider trackSchedulerProvider,
                 final CommandHelper commandHelper) {
        this.audioPlayerProvider = audioPlayerProvider;
        this.trackSchedulerProvider = trackSchedulerProvider;
        this.commandHelper = commandHelper;
    }

    @Override
    @ServiceActivator(inputChannel = "queueChannel")
    public void execute(final Message message) {
        final List<String> args = commandHelper.getArgs(message.getContentRaw());
        final TrackScheduler trackScheduler = trackSchedulerProvider.getAudioEventAdapter(message.getGuild().getId());
        if (!args.isEmpty()) {
            if (args.get(0).toLowerCase().matches("clear|empty|dump")) {
                message.getTextChannel().sendMessage("Queue has been cleared!").queue();
                trackScheduler.clear();
            }
        } else {
            final LinkedBlockingQueue<AudioTrack> queue = trackScheduler.getQueue();
            final TextChannel textChannel = message.getTextChannel();
            if (queue.peek() == null) textChannel.sendMessage("The Queue is empty!").queue();
            final AudioPlayer audioPlayer = audioPlayerProvider.getPlayerForGuild(message.getGuild().getId()).getPlayer();
            final AudioTrack playingTrack = audioPlayer.getPlayingTrack();
            if (playingTrack != null) {
                final EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.addField("Currently Playing", playingTrack.getInfo().title, false);
                populateQueueEmbed(queue, embedBuilder);
                embedBuilder.setTitle("Current Queue");
                textChannel.sendMessage(embedBuilder.build()).queue();
            }

        }


    }

    private void populateQueueEmbed(final LinkedBlockingQueue<AudioTrack> queue,
                                    final EmbedBuilder embedBuilder) {
        queue.forEach(audioTrack -> embedBuilder.addField("Name", audioTrack.getInfo().title, false));
    }


}
