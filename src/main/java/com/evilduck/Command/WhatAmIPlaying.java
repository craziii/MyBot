package com.evilduck.Command;

import com.evilduck.Configuration.CommandConfiguration.GenericCommand;
import com.evilduck.Util.CommandHelper;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Message;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static net.dv8tion.jda.core.entities.Game.GameType.LISTENING;

public class WhatAmIPlaying implements GenericCommand {

    private final CommandHelper commandHelper;

    public WhatAmIPlaying(final CommandHelper commandHelper) {
        this.commandHelper = commandHelper;
    }

    @Override
    public void execute(org.springframework.messaging.Message<Message> message) {

        final List<String> args = commandHelper.getArgs(message.getPayload().getContentRaw());
        final Optional<String> whatImPlaying = args.size() >= 2 ? of(args.get(1)) : empty();

        if (!whatImPlaying.isPresent())
            message.getPayload()
                    .getTextChannel()
                    .sendMessage("You must specify a what Im playing!")
                    .queue();

        final Game gameImPlaying = Game.of(LISTENING, whatImPlaying.orElse("Listening..."));
        message.getPayload().getJDA().getPresence().setGame(gameImPlaying);

    }

}
