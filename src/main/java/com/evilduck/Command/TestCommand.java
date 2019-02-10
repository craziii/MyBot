package com.evilduck.Command;

import com.evilduck.Command.Interface.IsACommand;
import com.evilduck.Command.Interface.ManualCommand;
import com.evilduck.Entity.SessionEntity;
import com.evilduck.Repository.SessionRepository;
import com.evilduck.Util.CommandHelper;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@IsACommand
public class TestCommand implements ManualCommand {

    private final SessionRepository sessionRepository;
    private final CommandHelper commandHelper;

    @Autowired
    public TestCommand(final SessionRepository sessionRepository,
                       final CommandHelper commandHelper) {
        this.sessionRepository = sessionRepository;
        this.commandHelper = commandHelper;
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

    @Override
    @ServiceActivator(inputChannel = "testCommandChannel")
    public void execute(final org.springframework.messaging.Message<Message> message) {

        final String rawMessage = message.getPayload().getContentRaw();
        final List<String> args = commandHelper.getArgs(rawMessage);

        final Optional<SessionEntity> session = getSessionIfExists(message.getPayload().getMember());
        if (session.isPresent()) {
            message.getPayload()
                    .getTextChannel()
                    .sendMessage("I have found a session for you!\nYou saved: " +
                            session.get().getSavedDetail())
                    .queue();
            sessionRepository.deleteById(session.get().getId());
        } else {
            if (args.get(1).equalsIgnoreCase("save")) sessionRepository.save(new SessionEntity(
                    message.getPayload().getMember().getUser().getId(),
                    commandHelper.getArgsAsString(rawMessage, 2)
            ));
        }

    }

    private Optional<SessionEntity> getSessionIfExists(final Member member) {
        return sessionRepository.findById(member.getUser().getId());
    }

}
