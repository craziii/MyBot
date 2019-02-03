package com.evilduck.Command;

import com.evilduck.Configuration.MessageHandling.GenericCommand;
import com.evilduck.Util.CommandHelper;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static net.dv8tion.jda.core.entities.Game.GameType.LISTENING;

//  TODO: WORK IN PROGRESS!
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
                    .sendMessage("You must specify a what i'm playing!")
                    .queue();

        message.getPayload().getJDA().getPresence().setGame(Game.of(LISTENING, whatImPlaying.orElse("Listening...")));

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
