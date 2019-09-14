package com.evilduck.command.audio;

import com.evilduck.command.interfaces.IsACommand;
import com.evilduck.command.interfaces.PrivateCommand;
import com.evilduck.configuration.audio.CacheableAudioPlayerProvider;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
@IsACommand(description = "Pauses the currently playing song", tutorial = "Use !pause whilst a song is playing")
public class Pause implements PrivateCommand {

    private final CacheableAudioPlayerProvider audioPlayerProvider;

    @Autowired
    public Pause(final CacheableAudioPlayerProvider audioPlayerProvider) {
        this.audioPlayerProvider = audioPlayerProvider;
    }


    @Override
    public boolean hasPermissionToRun(Member requestingMember) {
        return false;
    }

    @Override
    @ServiceActivator(inputChannel = "pauseChannel")
    public void execute(Message message) {
        final AudioPlayer audioPlayer = audioPlayerProvider.getPlayerForGuild(message.getGuild().getId()).getPlayer();
        audioPlayer.setPaused(!audioPlayer.isPaused());
    }
}
