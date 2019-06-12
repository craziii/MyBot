package com.evilduck.command.audio;

import com.evilduck.command.standards.IsACommand;
import com.evilduck.command.standards.PrivateCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
@IsACommand
public class Pause implements PrivateCommand {

    private final AudioPlayer audioPlayer;

    @Autowired
    public Pause(final AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    @Override
    public boolean hasPermissionToRun(Member requestingMember) {
        return false;
    }

    @Override
    @ServiceActivator(inputChannel = "pauseChannel")
    public void execute(Message message) {
        audioPlayer.setPaused(!audioPlayer.isPaused());
    }
}