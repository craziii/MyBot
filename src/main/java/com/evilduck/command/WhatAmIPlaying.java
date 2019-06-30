package com.evilduck.command;

import com.evilduck.command.standards.IsACommand;
import com.evilduck.command.standards.PrivateCommand;
import com.evilduck.util.CommandHelper;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.util.List;

import static net.dv8tion.jda.core.entities.Game.GameType.DEFAULT;
import static net.dv8tion.jda.core.entities.Game.GameType.LISTENING;
import static net.dv8tion.jda.core.entities.Game.GameType.STREAMING;
import static net.dv8tion.jda.core.entities.Game.GameType.WATCHING;

//  TODO: WORK IN PROGRESS!
@Component
@IsACommand(aliases = {"waip"})
public class WhatAmIPlaying implements PrivateCommand {

    private final CommandHelper commandHelper;

    public WhatAmIPlaying(final CommandHelper commandHelper) {
        this.commandHelper = commandHelper;
    }

    @Override
    @ServiceActivator(inputChannel = "whatAmIPlayingChannel")
    public void execute(final Message message) {

        final List<String> args = commandHelper.getArgs(message.getContentRaw());

        // TODO: Don't like how this is setup/working, perhaps it's time to go back to the drawing board???
        final Game whatIAmPlaying;
        final GameType gameType;
        final String gameArg;

        if (args.size() > 1) {
            final String gameTypeArg = args.get(0);
            gameType = gameTypeArg.matches("(?i:listen.*)") ?
                    LISTENING : gameTypeArg.matches("(?i:stream.*)") ?
                    STREAMING : gameTypeArg.matches("(?i:watch.*)") ?
                    WATCHING : DEFAULT;

            gameArg = commandHelper.getArgsAsAString(args, 2);
            whatIAmPlaying = gameType.equals(STREAMING) ? Game.of(gameType, gameArg, "www.fuckyou.com") : Game.of(gameType, gameArg);
        } else if (args.size() == 1) {
            gameArg = commandHelper.getArgsAsAString(args, 1);
            whatIAmPlaying = Game.of(DEFAULT, gameArg);

        } else {
            message.getTextChannel()
                    .sendMessage("You must specify a what i'm playing!")
                    .queue();
            return;
        }

        message.getJDA()
                .getPresence()
                .setGame(whatIAmPlaying);


        message.getTextChannel()
                .sendMessage(new EmbedBuilder().setTitle("I have changed what I'm doing!")
                        .setDescription("I am now " +
                                (
                                        whatIAmPlaying.getType().equals(DEFAULT) ? "playing" :
                                                whatIAmPlaying.getType().name().toLowerCase()
                                ) + " " + whatIAmPlaying.getName()).build())
                .queue();

    }

    @Override
    public boolean hasPermissionToRun(Member requestingMember) {
        return false;
    }

}
