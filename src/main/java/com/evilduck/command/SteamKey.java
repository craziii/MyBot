package com.evilduck.command;

import com.evilduck.command.standards.IsACommand;
import com.evilduck.command.standards.PublicCommand;
import com.evilduck.util.CommandHelper;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Integer.parseInt;
import static org.joda.time.DateTime.now;

@Component
@IsACommand(aliases = {"sk"})
public class SteamKey implements PublicCommand {

    private static final int COUNTDOWN_DELAY = 10;

    private final CommandHelper commandHelper;

    public SteamKey(final CommandHelper commandHelper) {
        this.commandHelper = commandHelper;
    }

    @Override
    @ServiceActivator(inputChannel = "steamKeyChannel")
    public void execute(Message message) {
        final MessageChannel originChannel = message.getTextChannel();

        final List<String> args = commandHelper.getArgs(message.getContentRaw());
        if (args.isEmpty()) {
            originChannel.sendMessage("Please provide a steam key to use this command!").queue();
            return;
        }

        final String steamKey = args.get(0);
        final int expiry = args.size() > 1 ? parseInt(args.get(1)) : -1;

        originChannel.deleteMessageById(message.getId()).queue();
        final String messageId = originChannel.sendMessage("@here Dropping steam key in: " + 10 + "s...").complete().getId();

        final Timer timer = new Timer();

        for (int i = COUNTDOWN_DELAY; i >= 0; i--)
            timer.schedule(new SteamKeyCountDown(originChannel, messageId, i, steamKey),
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
                    .editMessage(countDownTimer == 0 ? "Here is the steam key!\n```" + key + "```\n" : "@here Dropping steam key in: " + countDownTimer + "s...")
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