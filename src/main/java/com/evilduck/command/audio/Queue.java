package com.evilduck.command.audio;

import com.evilduck.command.standards.IsACommand;
import com.evilduck.command.standards.PublicCommand;
import com.evilduck.configuration.audio.TrackScheduler;
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
@IsACommand(aliases = "q", description = "Displays all audio tracks as a queue")
public class Queue implements PublicCommand {

    private final TrackScheduler trackScheduler;
    private final CommandHelper commandHelper;
    private final AudioPlayer audioPlayer;

    @Autowired
    public Queue(final TrackScheduler trackScheduler,
                 final CommandHelper commandHelper,
                 final AudioPlayer audioPlayer) {
        this.trackScheduler = trackScheduler;
        this.commandHelper = commandHelper;
        this.audioPlayer = audioPlayer;
    }

    @Override
    @ServiceActivator(inputChannel = "queueChannel")
    public void execute(final Message message) {
        final List<String> args = commandHelper.getArgs(message.getContentRaw());

        if (!args.isEmpty()) {
            if (args.get(0).toLowerCase().matches("clear|empty|dump")) {
                message.getTextChannel().sendMessage("Queue has been cleared!").queue();
                clearQueue();
            }
        } else {
            final LinkedBlockingQueue<AudioTrack> queue = trackScheduler.getQueue();
            final TextChannel textChannel = message.getTextChannel();
            if (queue.peek() == null) textChannel.sendMessage("The Queue is empty!").queue();
            else {
                final AudioTrack playingTrack = audioPlayer.getPlayingTrack();
                final EmbedBuilder embedBuilder = new EmbedBuilder();
                if (playingTrack != null)
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

    private void clearQueue() {
        trackScheduler.clear();
    }

}
