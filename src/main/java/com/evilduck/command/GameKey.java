package com.evilduck.command;

import com.evilduck.command.interfaces.IsACommand;
import com.evilduck.command.interfaces.PublicCommand;
import com.evilduck.util.CommandHelper;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import static org.joda.time.DateTime.now;

@Component
@IsACommand(description = "Used to display a message (usually a steam key) after some time delay ",
            tutorial = "Use !gamekey with the **place it can be redeemed**, " +
                    "**the key** and then **how long** you wish for it to be " +
                    "displayed (blank for forever) *(e.g. !gamekey gog hdy28-jdsig-dsj3s 30)*", aliases = {"gk"})
public class GameKey implements PublicCommand {

    private static final int COUNTDOWN_DELAY = 10;

    private final CommandHelper commandHelper;

    public GameKey(final CommandHelper commandHelper) {
        this.commandHelper = commandHelper;
    }

    @Override
    @ServiceActivator(inputChannel = "gameKeyChannel")
    public void execute(Message message) {
        final MessageChannel originChannel = message.getTextChannel();

        final List<String> args = commandHelper.getArgs(message.getContentRaw());
        if (args.isEmpty()) {
            originChannel.sendMessage("Please provide a steam key to use this command!").queue();
            return;
        }

        final String key = args.stream()
                .filter(cs -> !StringUtils.isNumeric(cs))
                .map(v -> v + " ")
                .collect(Collectors.joining(" "));

        final int expiry = args.stream()
                .filter(StringUtils::isNumeric)
                .map(Integer::parseInt)
                .findFirst()
                .orElse(-1);

        originChannel.deleteMessageById(message.getId()).queue();
        final String messageId = originChannel.sendMessage("@here Dropping game key in: " + 30 + "s...").complete().getId();

        final Timer timer = new Timer();

        for (int i = COUNTDOWN_DELAY; i >= 0; i--)
            timer.schedule(new SteamKeyCountDown(originChannel, messageId, i, key),
                    now().plusSeconds(COUNTDOWN_DELAY - i).toDate());

        if (expiry > 0)
            timer.schedule(new SteamKeyDelete(originChannel, messageId), now().plusSeconds(expiry + COUNTDOWN_DELAY).toDate());

    }

    private static class SteamKeyCountDown extends TimerTask {

        private final MessageChannel messageChannel;
        private final String messageId;
        private final int countDownTimer;
        private final String key;

        private SteamKeyCountDown(final MessageChannel messageChannel,
                                  final String messageId,
                                  final int countDownTimer, String key) {
            this.messageChannel = messageChannel;
            this.messageId = messageId;
            this.countDownTimer = countDownTimer;
            this.key = key;
        }


        @Override
        public void run() {
            messageChannel.getMessageById(messageId)
                    .complete()
                    .editMessage(countDownTimer == 0 ? "Here is the game key!\n```" + key + "```\n" : "@here Dropping steam key in: " + countDownTimer + "s...")
                    .queue();
        }
    }

    private static class SteamKeyDelete extends TimerTask {

        private final MessageChannel messageChannel;
        private final String messageId;

        private SteamKeyDelete(final MessageChannel messageChannel,
                               final String messageId) {
            this.messageChannel = messageChannel;
            this.messageId = messageId;
        }


        @Override
        public void run() {
            messageChannel.getMessageById(messageId).complete().delete().queue();
        }
    }

}
