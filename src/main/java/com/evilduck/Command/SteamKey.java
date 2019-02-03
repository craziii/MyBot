package com.evilduck.Command;

import com.evilduck.Command.Tools.IsACommand;
import com.evilduck.Configuration.MessageHandling.GenericCommand;
import com.evilduck.Util.CommandHelper;
import net.dv8tion.jda.core.entities.Member;
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
@IsACommand
public class SteamKey implements GenericCommand {

    private final CommandHelper commandHelper;

    public SteamKey(final CommandHelper commandHelper) {
        this.commandHelper = commandHelper;
    }

    @Override
    @ServiceActivator(inputChannel = "steamKeyChannel")
    public void execute(org.springframework.messaging.Message<Message> message) {
        final Message steamKeyMessage = message.getPayload();
        final MessageChannel originChannel = steamKeyMessage.getTextChannel();

        final List<String> args = commandHelper.getArgs(message.getPayload().getContentRaw());
        if (args.size() < 2) {
            originChannel.sendMessage("Please provide a steam key to use this command!").queue();
            return;
        }

        final String steamKey = args.get(1);
        final int expiry = args.size() > 2 ? parseInt(args.get(2)) : -1;


        originChannel.deleteMessageById(steamKeyMessage.getId()).queue();
        final String messageId = originChannel.sendMessage("Dropping steam key in: " + 5 + "s...").complete().getId();

        final Timer timer = new Timer();

        for (int i = 5; i >= 0; i--)
            timer.schedule(new SteamKeyCountDown(originChannel, messageId, i, steamKey),
                    now().plusSeconds(5 - i).toDate());

        if (expiry > 0)
            timer.schedule(new SteamKeyDelete(originChannel, messageId),
                    now().plusSeconds(expiry + 5).toDate());

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
                    .editMessage(countDownTimer == 0 ? "Here is the steam key!\n```" + key + "```\n" : "Dropping steam key in: " + countDownTimer + "s...")
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
