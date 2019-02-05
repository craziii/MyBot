package com.evilduck.Command;

import com.evilduck.Command.Tools.IsACommand;
import com.evilduck.Configuration.MessageHandling.ManualCommand;
import com.evilduck.Util.CommandHelper;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.util.List;

import static net.dv8tion.jda.core.entities.Game.GameType.*;

//  TODO: WORK IN PROGRESS!
@Component
@IsACommand
public class WhatAmIPlaying implements ManualCommand {

    private final CommandHelper commandHelper;

    public WhatAmIPlaying(final CommandHelper commandHelper) {
        this.commandHelper = commandHelper;
    }

    @Override
    @ServiceActivator(inputChannel = "whatAmIPlayingChannel")
    public void execute(org.springframework.messaging.Message<Message> message) {

        final List<String> args = commandHelper.getArgs(message.getPayload().getContentRaw());

        final Game whatIAmPlaying;
        final GameType gameType;
        final String gameArg;

        if (args.size() > 2) {
            final String gameTypeArg = args.get(1);
            gameType = gameTypeArg.matches("(?i:listen.*)") ?
                    LISTENING : gameTypeArg.matches("(?i:stream.*)") ?
                    STREAMING : gameTypeArg.matches("(?i:watch.*)") ?
                    WATCHING : DEFAULT;

            gameArg = commandHelper.getArgsAsAString(args, 2);
            whatIAmPlaying = gameType.equals(STREAMING) ? Game.of(gameType, gameArg, "www.fuckyou.com") : Game.of(gameType, gameArg);
        } else if (args.size() == 2) {
            gameArg = commandHelper.getArgsAsAString(args, 1);
            whatIAmPlaying = Game.of(DEFAULT, gameArg);

        } else {
            message.getPayload()
                    .getTextChannel()
                    .sendMessage("You must specify a what i'm playing!")
                    .queue();
            return;
        }

        message.getPayload()
                .getJDA()
                .getPresence()
                .setGame(whatIAmPlaying);


        message.getPayload().getTextChannel()
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

    @Override
    public void onSuccess(Message message) {

    }

    @Override
    public void onFail(Throwable throwable) {

    }

}
